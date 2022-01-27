package com.example.familyLocationTracker.views.requirementSetup

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentRequirementSetupBinding
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.permissionDenied
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast


class RequirementSetupFragment : BaseFragment<FragmentRequirementSetupBinding>() , View.OnClickListener
{

    /** By  Requirements setup  means checking boxes before showing map
     * GSP -> ON
     * Permissions  -> Allowed
     * **/

    var gps:Boolean = false
    var hasPermissions = false


    private val requirementSetupViewModel:RequirementSetupViewModel by viewModels()


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentRequirementSetupBinding
    {
        return FragmentRequirementSetupBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        initViews()


    } // onViewCreated closed

    override fun onResume()
    {
        super.onResume()
        requirementSetupViewModel.checkLocationPermissionsState()
        requirementSetupViewModel.checkGpsState()
    }

    private fun initViews()
    {

        requirementSetupViewModel.checkGpsState()
        requirementSetupViewModel.checkLocationPermissionsState()


        requirementSetupViewModel.hasPermission.observe(viewLifecycleOwner)
        {
            permission ->

            hasPermissions = permission

            if (permission)
            {
                    binding.fragmentRequirementSetupPermissionsButton.text = "ALLOWED"
            }else
            {
                binding.fragmentRequirementSetupPermissionsButton.text = "ALLOW"
            }

        } //

        requirementSetupViewModel.isGpsOn.observe(viewLifecycleOwner)
        {

            gps = it

            if (gps)
            {
                    binding.fragmentRequirementSetupGpsButton.text = "ON"
            }else
            {
                binding.fragmentRequirementSetupGpsButton.text = "OFF"
            }
        } //


        binding.fragmentRequirementSetupPermissionsButton.setOnClickListener(this)
        binding.fragmentRequirementSetupGpsButton.setOnClickListener(this)
        binding.fragmentRequirementSetupProceedButton.setOnClickListener(this)

    } // initViews closed

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentRequirementSetupPermissionsButton -> handlePermissions()
            R.id.fragmentRequirementSetupGpsButton -> handleGPs()
            R.id.fragmentRequirementSetupProceedButton -> proceed()
        }
    } // onClick closed

    private fun proceed()
    {
        if(!hasPermissions)
        {
            requireContext().showToast("Permissions not Granted Yet")
            return
        }
        if (!gps)
        {
            requireContext().showToast("GPS Not Enabled")
            return
        }

        findNavController().navigate(R.id.familyMapFragment)


    } // proceed

    private fun handlePermissions()
    {
        if(hasPermissions)
        {
            requireContext().showToast("Permissions Given")
        }else
        {
            permissionsCallback.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    } // handlePermissions closed

    private fun handleGPs()
    {
        if(hasPermissions)
        {
            requireContext().showToast("GPS is enabled")
        }else
        {
            requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    } // handleGps








    private val permissionsCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        {
            it ->

            it.forEach()
            {
                if(it.value)
                {
                    requirementSetupViewModel.checkLocationPermissionsState()
                }else
                {
                    requireContext().permissionDenied("App requires Location Permission")
                }
            }
        } // registerForActivityResult closed




}