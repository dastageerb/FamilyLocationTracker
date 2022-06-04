package com.example.familyLocationTracker.fcm

import androidx.annotation.AnyThread
import com.example.familyLocationTracker.models.notification.NotificationData
import com.example.familyLocationTracker.util.Constants
import com.google.android.gms.common.api.Response
import com.squareup.okhttp.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface
{



    //                    header.put("authorization", "key=" + Constants.FIREBASE_SERVER_KEY);
    @Headers
        ("Authorization: key=" + Constants.FIREBASE_SERVER_KEY, "Content-Type:application/json")
    @POST("fcm/send")
    suspend fun sendNotification(@Field("to") token:String, @Body notificationData: NotificationData):ResponseBody


}