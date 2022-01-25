package com.example.familyLocationTracker.views.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentUserProfileBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.RequestState
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.MainViewModel


class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() , View.OnClickListener
{

    val mainViewModel:MainViewModel by activityViewModels()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentUserProfileBinding
    {
        return FragmentUserProfileBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    } // onViewCreated closed


    private fun initViews()
    {
        mainViewModel.sharedUser.let()
        {
            binding.fragmentUserProfileUserNameTextView.text = mainViewModel.sharedUser!!.userName
            binding.fragmentUserProfileUserProfileImageView.load(it?.userImageUrl)
            controlVisibilityOfViews(false,false,false)
            mainViewModel.getRequestState(it?.userContact!!)
        }

        binding.fragmentUserProfileSendRequestButton.setOnClickListener(this)
        binding.fragmentUserProfileAcceptRequestButton.setOnClickListener(this)
        binding.fragmentUserProfileDeclineRequestButton.setOnClickListener(this)
        binding.fragmentUserProfileCancelRequestButton.setOnClickListener(this)

        mainViewModel.requestState.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is NetworkResponse.Loading ->
                {
                    binding.fragmentUserProfileProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentUserProfileProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentUserProfileProgressBar.hide()
                    whenRequestStateChanged(it.data)
                }
            } // when closed
        } // requestStateObserver


    } // initView closed

    private fun whenRequestStateChanged(data: RequestState?)
    {
        when(data)
        {
            RequestState.SEND -> controlVisibilityOfViews(true, sent = false, received = false)
            RequestState.SENT ->controlVisibilityOfViews(false, sent = true, received = false)
            RequestState.RECEIVED -> controlVisibilityOfViews(false, sent = false, received = true)
        }


    } // whenRequestStateChanged

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentUserProfileSendRequestButton ->
            {
                mainViewModel.sharedUser?.let()
                {
                    mainViewModel.sendRequest(it.userContact!!)
                }
            }
            R.id.fragmentUserProfileAcceptRequestButton ->
            {

            }
            R.id.fragmentUserProfileDeclineRequestButton ->
            {

            }
            R.id.fragmentUserProfileCancelRequestButton ->
            {

            }
        } // when closed
    } // onClick closed


    private fun controlVisibilityOfViews(send:Boolean,sent:Boolean,received:Boolean)
    {
        if(send)
        {
            binding.fragmentUserProfileSendRequestButton.show()
        }else
        {
            binding.fragmentUserProfileSendRequestButton.hide()
        }


        if (received)
        {
            binding.fragmentUserProfileReceivedRequestLayout.show()
        }else
        {
            binding.fragmentUserProfileReceivedRequestLayout.hide()
        }

        if(sent)
        {
            binding.fragmentUserProfileSentRequestLayout.show()
        }else
        {
            binding.fragmentUserProfileSentRequestLayout.hide()
        }


    } // controlVisibility


} // UserProfileFragment closed