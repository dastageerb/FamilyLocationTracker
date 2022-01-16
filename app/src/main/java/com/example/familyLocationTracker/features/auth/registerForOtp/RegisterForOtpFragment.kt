package com.example.familyLocationTracker.features.auth.registerForOtp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.features.auth.AuthViewModel
import com.example.familyLocationTracker.features.auth.model.VerificationEntity
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentRegisterForOtpBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.google.firebase.auth.FirebaseAuth


class RegisterForOtpFragment : BaseFragment<FragmentRegisterForOtpBinding>() , View.OnClickListener
{


    val viewModel:AuthViewModel by  activityViewModels()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentRegisterForOtpBinding
    {
        return FragmentRegisterForOtpBinding.inflate(inflater,container,false)
    } // createViewClosed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initViews()


    } // onViewCreated closed

    private fun initViews()
    {
        binding.fragmentRegisterForOtpSendOtpButton.setOnClickListener(this)

        if(FirebaseAuth.getInstance().currentUser != null)
        {
            findNavController().setGraph(R.navigation.setup_profile_fragment)
         //   findNavController().setGraph(R.navigation.requirement_setup_nav)
        }


    }


    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentRegisterForOtpSendOtpButton -> sendOtp()
        } // when closed
    } // onClick closed

    private fun sendOtp()
    {
        val number = binding.fragmentRegisterForOtpNumberEditText.text.toString()
        viewModel.sendFirebaseOtp(number,requireActivity())


        // subscribe  to response observer
        viewModel.userOtpResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is NetworkResponse.Loading ->
                {
                    binding.fragmentRegisterProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentRegisterProgressBar.hide()
                    requireContext().showToast(it.message!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentRegisterProgressBar.hide()
                    setVerificationDataAndNavigation(it.data)
                } // success closed
            } // when closed
        } // viewLifeCycleOwner

    } // sendOtp closed

    private fun setVerificationDataAndNavigation(verificationId: String?)
    {
        val number = binding.fragmentRegisterForOtpNumberEditText.text.toString()
        val verificationEntity = VerificationEntity(verificationId!!,number)
        viewModel.setVerificationEntity(verificationEntity)
        findNavController().navigate(R.id.action_registerForOtpFragment_to_otpVerificationFragment)

    }


} /// RegisterForOtp closed