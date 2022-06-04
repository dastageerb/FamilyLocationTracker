package com.example.familyLocationTracker.fcm

import com.example.familyLocationTracker.util.Constants.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FCMService : FirebaseMessagingService()
{


    override fun onMessageReceived(message: RemoteMessage)
    {
        super.onMessageReceived(message)

        Timber.tag(TAG).d(message.data["title"])

    }

    override fun onNewToken(p0: String)
    {
        super.onNewToken(p0)
    }

}