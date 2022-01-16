package com.example.familyLocationTracker.util.extensionFunctions

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
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












}