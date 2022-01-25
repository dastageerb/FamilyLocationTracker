package com.example.familyLocationTracker.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.models.User
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.NetworkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
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



    // for sharedCommunication Between Fragments
    lateinit var sharedUser: User




}