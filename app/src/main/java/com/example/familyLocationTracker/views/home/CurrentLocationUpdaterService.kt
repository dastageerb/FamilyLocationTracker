package com.example.familyLocationTracker.views.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.example.familyLocationTracker.util.Constants
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*

class CurrentLocationUpdaterService :LifecycleService()
{


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null


    fun LocationUpdaterService()
    {
    }

    @SuppressLint("MissingPermission")
//    override fun onCreate()
//    {
//        super.onCreate()
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        locationRequest = LocationRequest.create()
//        locationRequest!!.interval = 30000
//        locationRequest!!.fastestInterval = 20000
//        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val locationCallback: LocationCallback = object : LocationCallback()
//        {
//            override fun onLocationResult(locationResult: LocationResult)
//            {
//                val locationIntent = Intent()
//                locationIntent.putExtra("latitude", locationResult.lastLocation.latitude)
//                locationIntent.putExtra("longitude", locationResult.lastLocation.longitude)
//                locationIntent.action = "FILTER"
//                sendBroadcast(locationIntent)
//                updateDataBase(
//                    locationResult.lastLocation.latitude,
//                    locationResult.lastLocation.longitude
//                )
//            }
//        }
//        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest!!, locationCallback, Looper.getMainLooper())
//
//
//    } // onCreate closed
//

//    private fun updateDataBase(latitude: Double, longitude: Double)
//    {
//        val documentReference = FirebaseDatabase.getInstance().reference
//        val time = DateFormat.getDateTimeInstance().format(Date())
//        documentReference
//            .child(Constants.DRIVER_CURRENT_LOCATION)
//            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
//            .setValue(TrackingData(latitude, longitude, time))
//    } // updateDataBase
//



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }


    override fun onDestroy()
    {
        super.onDestroy()
    }


}