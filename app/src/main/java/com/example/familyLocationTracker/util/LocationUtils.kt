package com.example.familyLocationTracker.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

object LocationUtils
{


    @SuppressLint("MissingPermission")
    suspend fun Context.getCurrentLocation(): Location
    {
        val fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)
        return fusedLocationProviderClient.lastLocation.await()
    } // getCurrentLocation



    fun Context.getLocalityFromLatLng(latLng: LatLng): String?
    {
        try
        {
            val gcd = Geocoder(this, Locale.getDefault())
            val addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.size > 0)
            {
                return addresses[0]
                    .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            }
        }
        catch (e: Exception)
        {
            return ""
        }
        return ""
    } // getLocalityFromLatLng







}