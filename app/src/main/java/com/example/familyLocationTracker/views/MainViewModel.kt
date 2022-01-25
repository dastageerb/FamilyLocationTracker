package com.example.familyLocationTracker.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.models.request.Request
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

class MainViewModel:ViewModel()
{


    private var firebaseFirestore:FirebaseFirestore?=null
    private var firebaseAuth:FirebaseAuth?=null

    init
    {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }


    // same live data variable will be used for alll users , searched users and friends responses
    private val _getUsersResponse :MutableLiveData<NetworkResponse<List<User>>> = MutableLiveData();
    val getUsersResponse :LiveData<NetworkResponse<List<User>>> = _getUsersResponse

    fun getAllUsers()  = viewModelScope.launch()
    {
        _getUsersResponse.value = NetworkResponse.Loading()

        try
        {


//            val allUsers = firebaseFirestore?.collection(Constants.USER_COLLECTION)
//                ?.whereNotEqualTo("userContact",firebaseAuth?.currentUser?.phoneNumber)
//                ?.get()?.await()
//
//            allUsers?.forEach()
//            {
//                Log.d(TAG, "All "+it.toObject(User::class.java))
//            }
//
//
//
//            val friends =  firebaseFirestore?.collection(Constants.USER_COLLECTION)
//                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
//                ?.collection(Constants.FRIENDS_COLLECTION)
//                ?.get()?.await()
//
//
//            friends?.forEach()
//            {
//
//                Log.d(TAG, "Friends: "+it.toObject(User::class.java))
//            }
//
//            val list = mutableListOf<User>()
//
//            friends?.intersect(allUsers!!)?.forEach()
//            {
//                list.add(it.toObject(User::class.java))
//
//                Log.d(TAG, "Intersect: "+it.toObject(User::class.java))
//
//            }
//            _getUsersResponse.value = NetworkResponse.Success(list)


//
            firebaseFirestore?.collection(Constants.USER_COLLECTION)
                ?.whereNotEqualTo("userContact",firebaseAuth?.currentUser?.phoneNumber)
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






}