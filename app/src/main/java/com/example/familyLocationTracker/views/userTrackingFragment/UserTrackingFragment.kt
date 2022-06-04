package com.example.familyLocationTracker.views.userTrackingFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentUserTrackingBinding
import com.example.familyLocationTracker.models.notification.NotificationData


class UserTrackingFragment : BaseFragment<FragmentUserTrackingBinding>()
{

    val viewModel:UserTrackingViewModel by activityViewModels()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentUserTrackingBinding
    {
       return FragmentUserTrackingBinding.inflate(inflater,container,false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)



        viewModel?.sharedUser?.let()
        {

            viewModel
                .sendNotification(it.fcmToken!!, NotificationData("want to see your location","Location Request","+9230733474"))

        } //

    } // onViewCreated


}