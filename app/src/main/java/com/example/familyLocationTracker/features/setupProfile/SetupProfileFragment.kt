package com.example.familyLocationTracker.features.setupProfile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.lifecycleScope
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentRequirementSetupBinding
import com.example.familyLocationTracker.databinding.FragmentSetupProfileBinding
import com.example.familyLocationTracker.util.LocationUtils.getCurrentLocation
import com.example.familyLocationTracker.util.LocationUtils.getLocalityFromLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>() , View.OnClickListener , OnMapReadyCallback
{

    lateinit var googleMap: GoogleMap
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentSetupProfileBinding
    {
        return FragmentSetupProfileBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentSetupProfileMapView.onCreate(savedInstanceState)
        binding.fragmentSetupProfileGetCurrentLocationButton.setOnClickListener(this)
        binding.fragmentSetupProfileMapView.getMapAsync(this)

    } // onViewCreated

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
        } //  when closed
    } // onClick closed

    private fun getUserCurrentLocation()
    {
        lifecycleScope.launch()
        {

            val location = requireContext().getCurrentLocation()
            val currentLocationLatLng = LatLng(location.latitude, location.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 14.0f))
            val address = requireContext().getLocalityFromLatLng(currentLocationLatLng)
            binding.fragmentSetupProfileAddressTextView.text = address

        } // lifecycleScope closed


    } // getUserCurrentLocation



} // setupProfileFragment