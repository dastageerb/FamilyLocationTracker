package com.example.familyLocationTracker.util.extensionFunctions

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.models.user.UserLocation
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.prefs.SharedPrefsHelper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*

object ContextExtension
{



    /******* Toast *******/

    fun Context.showToast(msg:String) = Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()


    /******* GPS *******/

    fun Context.gpsEnabled(): Boolean
    {
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } // gpsEnabled



    /******* Internet *******/

    fun Context.hasInternetConnection() : Boolean
    {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when
        {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        } // return When closed
    } // hasInternet closed





    /******* Dialog *******/

    fun Context.showDialog(message:String)
    {
        // notify user
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok")
            {
                    paramDialogInterface, _ ->
                paramDialogInterface.dismiss()
            }
            .show()
    } // checkGps

     /******* Permissions *******/

    fun Context.locationPermissionsGranted(): Boolean
    {
        return !(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) // if closed

    } // checkIfPermissionsGiven




     fun Context.permissionDenied(msg:String)
    {
        this.showToast(msg)
        this.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply ()
            {
                data = Uri.fromParts("package", Constants.APP_PACKAGE_NAME, null)
            })
    }// permissionDenied


    fun Context.hasStoragePermission():Boolean
    {
        return  (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

//
//    fun Context.hasCameraPermission():Boolean
//    {
//        return  (ContextCompat.checkSelfPermission(this,
//            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//    }

    @SuppressLint("MissingPermission")
    fun Context.uploadUserLocation()
    {
        Timber.tag(Constants.TAG).d("worker Working inside ")
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser!=null)
        {

            var fusedLocationProviderClient: FusedLocationProviderClient? = null
            var locationRequest: LocationRequest? = null
            var locationCallback: LocationCallback?=null
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            locationRequest = LocationRequest.create()
            locationRequest.interval = 1000
            locationRequest.fastestInterval = 500
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            locationCallback = object : LocationCallback()
            {
                override fun onLocationResult(locationResult: LocationResult)
                {
                    val latitude =locationResult.lastLocation.latitude
                    val longitude = locationResult.lastLocation.longitude
                    //val latlng = com.google.android.gms.maps.model.LatLng(latitued,longitude)
                    //Timber.tag(TAG).d(latlng.toString())
                    val user = SharedPrefsHelper(this@uploadUserLocation).getUser()
                    val userLocation = UserLocation(latitude,longitude)
                    user?.userLocation = userLocation
                    firebaseFirestore
                        .collection(Constants.USER_COLLECTION)
                        .document(firebaseAuth.currentUser?.phoneNumber!!)
                        .update("userLocation",userLocation)

                    fusedLocationProviderClient.removeLocationUpdates(locationCallback!!)

                }
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())




        } // currentUser closed


    } // uploadUserLocation








}