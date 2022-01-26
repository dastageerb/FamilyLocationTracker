package com.example.familyLocationTracker.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentOwnProfileBinding
import com.google.firebase.auth.FirebaseAuth


class OwnProfileFragment : BaseFragment<FragmentOwnProfileBinding>() , View.OnClickListener
{


    val viewModel:SetupProfileViewModel by viewModels()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentOwnProfileBinding
    {
        return FragmentOwnProfileBinding.inflate(inflater,container,false)
    } // createView closed


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getUser().let()
        {
            binding.fragmentOwnProfileUserProfileImageView.load(it?.userImageUrl)

            binding.fragmentOwnProfileUserNameTextView.text = it?.userName
            binding.fragmentOwnProfileUserContactTextView.text = it?.userContact
            binding.fragmentOwnProfileUserCityTextView.text = it?.userCity

        }

        binding.fragmentOwnProfileLogoutButton.setOnClickListener(this)

    } // onViewCreated cosed

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            R.id.fragmentOwnProfileLogoutButton ->
            {
                viewModel.deleteUser()
                FirebaseAuth.getInstance().signOut()
                findNavController().setGraph(R.navigation.auth_nav)
            }

        } // when closed
    } // onClick closed

} // OwnProfileFragment closed