package com.example.familyLocationTracker.views.userTrackingFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.fcm.ApiClient
import com.example.familyLocationTracker.models.notification.NotificationData
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants.TAG
import kotlinx.coroutines.launch
import timber.log.Timber

class UserTrackingViewModel(application: Application):AndroidViewModel(application)
{


    fun sendNotification(token:String,notificationData: NotificationData) = viewModelScope.launch()
    {
        try
        {
            val responseBody= ApiClient.api?.sendNotification(token,notificationData)
            Timber.tag(TAG).d(""+responseBody)
        }catch (e:Exception)
        {
            Timber.tag(TAG).d(""+e.message)
        }

    }





    // for sharedCommunication Between Fragments
    var sharedUser: User?=null






}