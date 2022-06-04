package com.example.familyLocationTracker.views.viewModels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import androidx.work.*
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.models.user.UserLocation
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.prefs.SharedPrefsHelper
import com.example.familyLocationTracker.views.home.HomeWorker
import com.google.android.gms.location.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import com.google.type.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application):AndroidViewModel(application)
{


    private var firebaseFirestore:FirebaseFirestore?=null
    private var firebaseAuth:FirebaseAuth?=null
    private var sharedPrefsHelper:SharedPrefsHelper?=null
    init
    {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPrefsHelper = SharedPrefsHelper(application.applicationContext)
    }


    // same live data variable will be used for alll users , searched users and friends responses
    private val _getUsersResponse :MutableLiveData<NetworkResponse<List<User>>> = MutableLiveData();
    val getUsersResponse :LiveData<NetworkResponse<List<User>>> = _getUsersResponse





    fun getAllUsersOtherThanFriends()  = viewModelScope.launch()
    {
        _getUsersResponse.value = NetworkResponse.Loading()

        try
        {

            val peopleYouMayKnow = mutableListOf<User>()
            val userList = mutableListOf<User>()
            val friendsList = mutableListOf<User>()


            // fetch All Users
            val allUsers = firebaseFirestore?.collection(Constants.USER_COLLECTION)
                ?.whereNotEqualTo("userContact",firebaseAuth?.currentUser?.phoneNumber)
                ?.get()?.await()

            // fetch friends
            val friends =  firebaseFirestore?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.FRIENDS_COLLECTION)
                ?.get()?.await()


            // add all users to list
            allUsers?.forEach()
            {
                userList.add(it.toObject(User::class.java))
            }

            // add all friends in a list
            friends?.forEach()
            {
                friendsList.add(it.toObject(User::class.java))
            }

            // get the difference and add it to peopleYouMayKnow List
            peopleYouMayKnow.addAll(userList.minus(friendsList))



            _getUsersResponse.value = NetworkResponse.Success(peopleYouMayKnow)




        }catch (e:Exception)
        {
            _getUsersResponse.value = NetworkResponse.Error(e.message)
        }
    } // getAllUsers closed

    fun getFriends()  = viewModelScope.launch()
    {
        _getUsersResponse.value = NetworkResponse.Loading()

        try
        {
            firebaseFirestore?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.FRIENDS_COLLECTION)
                ?.get()
                ?.addOnCompleteListener()
                {
                        task ->
                    if(task.isSuccessful)
                    {
                        val list = mutableListOf<User>()
                        task.result.forEach()
                        {
                            list.add(it.toObject(User::class.java))
                        }
                        _getUsersResponse.value = NetworkResponse.Success(list)
                    }else
                    {
                        _getUsersResponse.value = NetworkResponse.Error(task.exception?.message)
                    } // else closed
                } // addOnCompleteLister closed

        }catch (e:Exception)
        {
            _getUsersResponse.value = NetworkResponse.Error(e.message)
        }
    } // getAllUsers closed




    fun getAllRequests()  = viewModelScope.launch()
    {
        _getUsersResponse.value = NetworkResponse.Loading()

        try
        {
            firebaseFirestore?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.get()
                ?.addOnCompleteListener()
                {
                        task ->
                    if(task.isSuccessful)
                    {
                        val list = mutableListOf<User>()
                        task.result.forEach()
                        {
                            list.add(it.toObject(User::class.java))
                        }
                        _getUsersResponse.value = NetworkResponse.Success(list)
                    }else
                    {
                        _getUsersResponse.value = NetworkResponse.Error(task.exception?.message)
                    } // else closed
                } // addOnCompleteLister closed

        }catch (e:Exception)
        {
            _getUsersResponse.value = NetworkResponse.Error(e.message)
        }
    } // getAllUsers closed



    @SuppressLint("MissingPermission")
    fun uploadUserLocation(context: Context) = viewModelScope.launch(Dispatchers.IO)
    {

        var fusedLocationProviderClient: FusedLocationProviderClient? = null
        var locationRequest: LocationRequest? = null
        var locationCallback:LocationCallback?=null

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
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

                val user = sharedPrefsHelper?.getUser()

                val userLocation = UserLocation(latitude,longitude)
                user?.userLocation = userLocation


                firebaseFirestore
                    ?.collection(Constants.USER_COLLECTION)
                    ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                    ?.set(user!!)

                fusedLocationProviderClient.removeLocationUpdates(locationCallback!!)

            }
        }


        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


    } // uploadUserLocation



    fun startWorker(context: Context)
    {

        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshCpnWork = PeriodicWorkRequest.Builder(HomeWorker::class.java, 2, TimeUnit.HOURS)
            .setConstraints(myConstraints)
            .addTag("myWorkManager")
            .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("myWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork)


    } // startWorker


}