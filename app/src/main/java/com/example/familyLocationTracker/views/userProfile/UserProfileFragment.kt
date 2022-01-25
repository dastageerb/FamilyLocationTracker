package com.example.familyLocationTracker.views.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentUserProfileBinding


class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>()
{



    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentUserProfileBinding
    {
        return FragmentUserProfileBinding.inflate(inflater,container,false)
    } // createView closed


} // UserProfileFragment closed