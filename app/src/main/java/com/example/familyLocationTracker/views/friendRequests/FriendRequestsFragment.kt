package com.example.familyLocationTracker.views.friendRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentFriendRequestsBinding


class FriendRequestsFragment : BaseFragment<FragmentFriendRequestsBinding>()
{


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentFriendRequestsBinding
    {
        return  FragmentFriendRequestsBinding.inflate(inflater,container,false)
    } // createView closed



} // FriendRequestsFragment closed