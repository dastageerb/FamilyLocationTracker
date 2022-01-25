package com.example.familyLocationTracker.views.setupProfile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentSetupProfileBinding
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.ImageUtils
import com.example.familyLocationTracker.util.LocationUtils.getCurrentLocation
import com.example.familyLocationTracker.util.LocationUtils.getLocalityFromLatLng
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.gpsEnabled
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.hasStoragePermission
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.locationPermissionsGranted
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.permissionDenied
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showDialog
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.disable
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.enable
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import timber.log.Timber


class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>() , View.OnClickListener , OnMapReadyCallback
{


    lateinit var googleMap: GoogleMap
    lateinit var currentLocationLatLng:LatLng
    lateinit var imageUri:Uri

    val viewModel:SetupProfileViewModel by viewModels()
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentSetupProfileBinding
    {
        return FragmentSetupProfileBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentSetupProfileMapView.onCreate(savedInstanceState)


        initViews()

    } // onViewCreated

    private fun initViews()
    {
        binding.fragmentSetupProfileGetCurrentLocationButton.setOnClickListener(this)
        binding.setupProfileFragmentImageView.setOnClickListener(this)
        binding.fragmentSetupProfileSetupProfileButton.setOnClickListener(this)
        binding.fragmentSetupProfileMapView.getMapAsync(this)

        val userNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber
        binding.fragmentSetupProfileNumberEditText.setText(userNumber)
        binding.fragmentSetupProfileNumberEditText.disable()


        viewModel.userUploadResponse.observe(viewLifecycleOwner)
        {

            when(it)
            {
                is NetworkResponse.Loading ->
                {
                    binding.fragmentSetupProfileProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    requireContext().showToast(it.message!!)
                    binding.fragmentSetupProfileProgressBar.hide()
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentSetupProfileProgressBar.hide()
                    if(!requireContext().locationPermissionsGranted() || !requireContext().gpsEnabled())
                    {
                        findNavController().setGraph(R.navigation.requirement_setup_nav)
                    }else
                    {
                        findNavController().setGraph(R.navigation.base_app_nav)
                    }
                } // success closed
            }
        }


    } // initView closed

    override fun onStart()
    {
        super.onStart()
        binding.fragmentSetupProfileMapView.onStart()
    }

    override fun onStop()
    {
        super.onStop()

        binding.fragmentSetupProfileMapView.onStop()
    }

    override fun onResume()
    {
        super.onResume()

        binding.fragmentSetupProfileMapView.onResume()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding.fragmentSetupProfileMapView.onDestroy()
    }

    override fun onMapReady(map: GoogleMap)
    {
          googleMap = map
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled = true
    }


    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentSetupProfileGetCurrentLocationButton ->
            {
                   getUserCurrentLocation()
            }
            R.id.setupProfileFragmentImageView ->
            {
                if(requireContext().hasStoragePermission())
                {
                    getImageFromGallery()
                } // if closed
                else
                {
                    permissionsCallback.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            } // setupProfileFragmentImageView
            R.id.fragmentSetupProfileSetupProfileButton ->
            {
                setupProfile()
            }
        } //  when closed
    } // onClick closed

    private fun setupProfile()
    {
        val name = binding.fragmentSetupProfileNameEditText.text.toString()
        val contact = binding.fragmentSetupProfileNumberEditText.text.toString()
        val city = binding.fragmentSetupProfileAddressTextView.text.toString()

        if(name.isEmpty())
        {
            binding.fragmentSetupProfileNameEditText.error = "Name is empty"
            return
        }
        if(!this::imageUri.isInitialized)
        {
            requireContext().showDialog("Please Pick User Image")
            return
        }

        viewModel.uploadUser(imageUri,name,contact,city,currentLocationLatLng)


    } // setupProfile closed

    private fun getUserCurrentLocation()
    {
        if(requireContext().locationPermissionsGranted())
        {
            if(requireContext().gpsEnabled())
            {
                lifecycleScope.launch()
                {
                    val currentLocation = requireContext().getCurrentLocation()
                     currentLocationLatLng = LatLng(currentLocation.latitude,currentLocation.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 14.0f))
                    val city = requireContext().getLocalityFromLatLng(currentLocationLatLng)

                    binding.fragmentSetupProfileAddressTextView.text = city
                    Timber.tag(Constants.TAG).d("$city")

                    binding.fragmentSetupProfileLocationLayout.show()
                    binding.fragmentSetupProfileSetupProfileButton.enable()

                } // lifecycleScope closed
            }else
            {
              requireContext().showDialog("Please Turn on Device Location")
            }
        }else
        {
            permissionsCallback.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    } // getUserCurrentLocation



    private val permissionsCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        {
            it.forEach()
            {
                entry ->
                when(entry.key)
                {
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION ->
                    {
                        if(entry.value)
                        {
                            getUserCurrentLocation()
                        }else
                        {
                            requireContext().permissionDenied("App requires Location Permission")
                        }
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE ->
                    {
                        if (entry.value)
                        {
                                getImageFromGallery()
                        }else
                        {
                            requireContext().permissionDenied("App requires Storage Access Permission")
                        } // else closed
                    } //
                } // when
            } // for Each closed
        } // registerForActivityResult closed




    private fun getImageFromGallery()
    {
        selectImageFromGalleryResult.launch("image/*")
    } // getImageFromGallery closed



    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri: Uri? ->
        uri?.let()
        {
            imageUri = it
            binding.setupProfileFragmentImageView.load(uri.toString())
            cropGalleryImage.launch(uri)
        } //
    } //

    private val cropGalleryImage = registerForActivityResult(ImageUtils.cropImage)
    {
        it?.let()
        { uri ->
            imageUri = uri
            binding.setupProfileFragmentImageView.load(uri.toString())
        }
    } // pickGalleryImage result closed







} // setupProfileFragment