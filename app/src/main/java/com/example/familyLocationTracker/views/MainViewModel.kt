package com.example.familyLocationTracker.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.models.request.Request
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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







    /*** GET User Request State ***/

    private val _requestState :MutableLiveData<NetworkResponse<RequestState>> = MutableLiveData();
    val requestState:LiveData<NetworkResponse<RequestState>> = _requestState




    fun sendRequest(userContact: String) = viewModelScope.launch()
    {

        /// userContact acts as user Id

        _requestState.value = NetworkResponse.Loading()
        try
        {

            val userCollection = firebaseFirestore
                ?.collection(Constants.REQUESTS_COLLECTION)
                ?.document(userContact)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)?.set(Request("received"))?.await()

            val myDb = firebaseFirestore
                ?.collection(Constants.REQUESTS_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.SENT_COLLECTION)
                ?.document(userContact)?.set(Request("sent"))?.await()

            _requestState.value = NetworkResponse.Success(RequestState.SENT)

        }catch (e:Exception)
        {
            _requestState.value = NetworkResponse.Error(e.message)
        }

    } //



    fun getRequestState(userContact:String) = viewModelScope.launch()
    {

        /// userContact acts as user Id

        _requestState.value = NetworkResponse.Loading()

        try
        {

            val request = firebaseFirestore
                ?.collection(Constants.REQUESTS_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.document(userContact)?.get()?.await()

            if(request!!.exists())
            {
                _requestState.value = NetworkResponse.Success(RequestState.RECEIVED)
            }else
            {
                _requestState.value = NetworkResponse.Success(RequestState.SEND)
            }


        }catch (e:Exception)
        {
            _requestState.value = NetworkResponse.Error(e.message)
        }


    } // getRequestState closedd



    // for sharedCommunication Between Fragments
    var sharedUser: User?=null




}