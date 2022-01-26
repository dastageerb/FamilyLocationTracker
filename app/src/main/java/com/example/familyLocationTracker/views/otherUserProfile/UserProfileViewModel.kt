package com.example.familyLocationTracker.views.otherUserProfile

import android.app.Application
import androidx.lifecycle.*
import com.example.familyLocationTracker.models.request.Request
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.RECEIVED_COLLECTION
import com.example.familyLocationTracker.util.Constants.SENT_COLLECTION
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.RequestState
import com.example.familyLocationTracker.util.prefs.SharedPrefsHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

class UserProfileViewModel(application: Application):AndroidViewModel(application)
{

    private var firebaseFirestore: FirebaseFirestore?=null
    private var firebaseAuth: FirebaseAuth?=null
    private var sharedPrefsHelper:SharedPrefsHelper?=null

    init
    {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPrefsHelper = SharedPrefsHelper(application.applicationContext)
    }


    /*** GET User Request State ***/

    private val _requestState : MutableLiveData<NetworkResponse<RequestState>> = MutableLiveData();
    val requestState: LiveData<NetworkResponse<RequestState>> = _requestState




    fun sendRequest(userContact: String) = viewModelScope.launch()
    {

        /// userContact acts as user Id

        _requestState.value = NetworkResponse.Loading()
        try
        {


                // in my sent collection
                firebaseFirestore
                    ?.collection(Constants.USER_COLLECTION)
                    ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                    ?.collection(Constants.SENT_COLLECTION)
                    ?.document(userContact)
                    ?.set(Request("sent"))
                    ?.await()

            // in user received collection
            firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(userContact)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)?.set(Request("received"))
                ?.await()



            _requestState.value = NetworkResponse.Success(RequestState.SENT)

        }catch (e: Exception)
        {
            _requestState.value = NetworkResponse.Error(e.message)
        }

    } //

    fun cancelOrDeclineFriendRequest(userContact: String) = viewModelScope.launch()
    {

        /// userContact acts as user Id
        _requestState.value = NetworkResponse.Loading()
        try
        {

            // in my sent collection
            firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.SENT_COLLECTION)
                ?.document(userContact)
                ?.delete()?.await()

            // in user received collection
            firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(userContact)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.delete()
                ?.await()


            _requestState.value = NetworkResponse.Success(RequestState.SEND)
        }catch (e: Exception)
        {
            _requestState.value = NetworkResponse.Error(e.message)
        }
    } //


    fun acceptFriendRequest(user:User) = viewModelScope.launch()
    {

        /// userContact acts as user Id
        _requestState.value = NetworkResponse.Loading()
        try
        {

        firebaseFirestore
            ?.collection(Constants.USER_COLLECTION)
            ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
            ?.collection(RECEIVED_COLLECTION)
            ?.document(user.userContact!!)
            ?.delete()?.await()


            firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(user.userContact!!)
                ?.collection(SENT_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)?.delete()?.await()


           // val myFriends =
                firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.FRIENDS_COLLECTION)
                ?.document(user.userContact!!)?.set(user)?.await()

            firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(user.userContact!!)
                ?.collection(Constants.FRIENDS_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)?.set(getUser()!!)?.await()


            _requestState.value = NetworkResponse.Success(RequestState.FRIENDS)
        }catch (e: Exception)
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

            val friend = firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.FRIENDS_COLLECTION)
                ?.document(userContact)
                ?.get()
                ?.await()

            if(friend?.exists()!!)
            {
                _requestState.value = NetworkResponse.Success(RequestState.FRIENDS)
                return@launch
            }

            val receivedRequest = firebaseFirestore
                ?.collection(Constants.USER_COLLECTION)
                ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                ?.collection(Constants.RECEIVED_COLLECTION)
                ?.document(userContact)
                ?.get()
                ?.await()

            if(receivedRequest!!.exists())
            {
                _requestState.value = NetworkResponse.Success(RequestState.RECEIVED)
            }else
            {

                val sentRequest = firebaseFirestore
                    ?.collection(Constants.USER_COLLECTION)
                    ?.document(firebaseAuth?.currentUser?.phoneNumber!!)
                    ?.collection(Constants.SENT_COLLECTION)
                    ?.document(userContact)
                    ?.get()
                    ?.await()

                if(sentRequest!!.exists())
                {
                    _requestState.value = NetworkResponse.Success(RequestState.SENT)
                }else
                {
                    _requestState.value = NetworkResponse.Success(RequestState.SEND)
                }
            }


        }catch (e: Exception)
        {
            _requestState.value = NetworkResponse.Error(e.message)
        }


    } // getRequestState closedd



    // for sharedCommunication Between Fragments
    var sharedUser: User?=null


    // sharedPrefs

    fun getUser() = sharedPrefsHelper?.getUser()

    fun saveUser(user: User) = sharedPrefsHelper?.saveUser(user)

    fun deleteUser() = sharedPrefsHelper?.clearUser()

} // UserProfileViewModel closed