package com.example.familyLocationTracker.views.addedFriendsProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentAddedFriendsProfileBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.viewModels.HandleRequestsViewModel


class AddedFriendsProfileFragment : BaseFragment<FragmentAddedFriendsProfileBinding>(),View.OnClickListener
{


    private val handleRequestsViewModel: HandleRequestsViewModel by activityViewModels()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentAddedFriendsProfileBinding
    {
        return FragmentAddedFriendsProfileBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        initViews()

    } // onViewCreated closed

    private fun initViews()
    {
        handleRequestsViewModel.sharedUser.let()
        {
            binding.fragmentAddedFriendUserNameTextView.text = handleRequestsViewModel.sharedUser!!.userName
            binding.fragmentAddedFriendsProfileProfileImageView.load(it?.userImageUrl)
        }

        binding.fragmentAddedFriendsProfileCallButton.setOnClickListener(this)
        binding.fragmentAddedFriendsProfileTextMessageButton.setOnClickListener(this)
        binding.fragmentAddedFriendsProfileUnFriendButton.setOnClickListener(this)
        binding.fragmentAddedFriendsProfileTrackButton.setOnClickListener(this)


        handleRequestsViewModel.requestState.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is NetworkResponse.Loading ->
                {
                    binding.fragmentAddedFriendProfileProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentAddedFriendProfileProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentAddedFriendProfileProgressBar.hide()
                    findNavController().navigate(R.id.action_addedFriendsProfileFragment_to_homeFragment)
                }
            } // when closed
        } // requestStateObserver




    } // initView closed

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentAddedFriendsProfileCallButton ->
            {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" +handleRequestsViewModel.sharedUser?.userContact)
                startActivity(intent)
            }
            R.id.fragmentAddedFriendsProfileTextMessageButton ->
            {
                val smsIntent = Intent(Intent.ACTION_SENDTO)
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
                smsIntent.data = Uri.parse("sms:" + handleRequestsViewModel.sharedUser?.userContact)
                startActivity(smsIntent)
            }

            R.id.fragmentAddedFriendsProfileUnFriendButton ->
            {
                handleRequestsViewModel.cancelOrDeclineFriendRequest(handleRequestsViewModel.sharedUser?.userContact!!)
            }

            R.id.fragmentAddedFriendsProfileTrackButton ->
            {

            }
        } // when closed
    } // onClick closed

} // AddedFriendProfileFragment