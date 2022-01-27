package com.example.familyLocationTracker.views.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.adapters.UserAdapter
import com.example.familyLocationTracker.adapters.UsersOnMap
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentFamilyMapBinding
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.LocationUtils.getCurrentLocation
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.gpsEnabled
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.locationPermissionsGranted
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.permissionDenied
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showDialog
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.viewModels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions





class FamilyMapFragment : BaseFragment<FragmentFamilyMapBinding>() ,OnMapReadyCallback
{

    val mainViewModel: MainViewModel by activityViewModels()
    lateinit var googleMap: GoogleMap
    lateinit var currentLocationLatLng: LatLng
    lateinit var adapter:UsersOnMap

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentFamilyMapBinding
    {

        if(!requireContext().gpsEnabled() || !requireContext().locationPermissionsGranted())
        {
            findNavController().setGraph(R.navigation.requirement_setup_nav)
        }

        return FragmentFamilyMapBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentFamilyMapMapView.onCreate(savedInstanceState)

        binding.fragmentFamilyMapMapView.getMapAsync(this)

        binding.fragmentFamilyMapRecyclerView.bringToFront()
        setupRecycler(binding.fragmentFamilyMapRecyclerView)
        initViews()

    }

    private fun initViews()
    {

        mainViewModel.getFriends()

        mainViewModel.getUsersResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {

                is NetworkResponse.Loading ->
                {
                  //  binding.fragmentHomeProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                   // binding.fragmentHomeProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                    adapter.submitList(it.data)
                    addMarkersToTheMap(it.data)
                }
            } // when closed
        } // observer closed


        binding.fragmentFamilyMapCardView.setOnClickListener()
        {
            if(binding.fragmentFamilyMapRecyclerView.visibility == View.VISIBLE)
            {
                    binding.fragmentFamilyMapRecyclerView.hide()
            }else
            {

                binding.fragmentFamilyMapRecyclerView.show()
            }


        }


    } // initViews() closed

    private fun addMarkersToTheMap(userList: List<User>?)
    {

        userList?.forEach()
        {
            val userLocation = it.userLocation

            val options = MarkerOptions()
                .position(LatLng(userLocation?.latitude!!,userLocation?.longitude!!))
                .title(it.userName)
               // .snippet(snips)
            googleMap //Google googleMap object for map reference
                .addMarker(options)?.tag = it.userContact
        } // userList forEach closed
    } // addMarkerToTheMaps

    private fun setupRecycler(recycler: RecyclerView)
    {
        adapter = UsersOnMap()
        {
            val location = it.userLocation;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location?.latitude!!,location?.longitude!!),10.0f));
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter


    } // setupRecyclerView closed


    override fun onMapReady(map: GoogleMap)
    {
        googleMap = map
        getUserCurrentLocation()

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
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_map_style));

    }


    override fun onStart()
    {
        super.onStart()
        binding.fragmentFamilyMapMapView.onStart()
    }

    override fun onStop()
    {
        super.onStop()

        binding.fragmentFamilyMapMapView.onStop()
    }

    override fun onResume()
    {
        super.onResume()

        binding.fragmentFamilyMapMapView.onResume()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding.fragmentFamilyMapMapView.onDestroy()
    }


    // getCurrentLocation closed

    private fun getUserCurrentLocation()
    {
        if(requireContext().locationPermissionsGranted())
        {
            if(requireContext().gpsEnabled())
            {
                lifecycleScope.launch()
                {

                    try
                    {

                        val currentLocation = requireContext().getCurrentLocation().await()
                        currentLocationLatLng = LatLng(currentLocation.latitude,currentLocation.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 1.0f))


                    }catch (e:Exception)
                    {
                        getUserCurrentLocation()
                    }

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
                    } //
                } // when
            } // for Each closed
        } // registerForActivityResult closed





} // FamilyMapsFragment closed