package com.example.familyLocationTracker.auth.otpVerification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.auth.AuthViewModel
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentOtpVerificationBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show


class OtpVerificationFragment : BaseFragment<FragmentOtpVerificationBinding>() , View.OnClickListener
{


    val viewModel: AuthViewModel by  activityViewModels()
    lateinit var verificationId:String

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentOtpVerificationBinding
    {
        return FragmentOtpVerificationBinding.inflate(inflater,container,false)
    } // createView closed


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)



        initViews()


    } /// onViewCreated closed

    private fun initViews()
    {
        viewModel.getSharedData.observe(viewLifecycleOwner)
        {
            verificationId = it.verificationId
            binding.fragmentOtpVerificationNumberTextView.text = it.number
        }

        binding.fragmentOtpVerificationVerifyButton.setOnClickListener(this)
    }

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentOtpVerificationVerifyButton -> verify()
        }
    }

    private fun verify()
    {

        val otpCode = binding.fragmentOtpVerificationPinViewEditText.text.toString()
        viewModel.verifyUser(verificationId,otpCode)

        viewModel.userOtpResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is NetworkResponse.Loading ->
                {
                    binding.fragmentOtpVerificationFragmentProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentOtpVerificationFragmentProgressBar.hide()
                    requireContext().showToast(it.message!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentOtpVerificationFragmentProgressBar.hide()
                    requireContext().showToast(it.data!!)



                    findNavController().setGraph(R.navigation.requirement_setup_nav)
                } // success closed
            } // when closed
        } // viewLifeCycleOwner



    } // verify closed


} // OtpVerificationFragment closed