package com.example.familyLocationTracker.views.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyLocationTracker.models.auth.VerificationEntity
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.NetworkResponse
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel()
{

    private var firebaseAuth: FirebaseAuth?=null
    private var firebaseFireStore:FirebaseFirestore?=null
    init
    {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()
    }

    /**   Sending Otp  */

    val _userOtpResponse:MutableLiveData<NetworkResponse<String>> = MutableLiveData()
    val userOtpResponse:LiveData<NetworkResponse<String>> = _userOtpResponse

    fun sendFirebaseOtp(number:String,activity: Activity) = viewModelScope.launch()
    {

        _userOtpResponse.value = NetworkResponse.Loading()

        val callback = object :  PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken)
            {
                super.onCodeSent(verificationId, p1)
                _userOtpResponse.value = NetworkResponse.Success(verificationId)
            }
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {}
            override fun onVerificationFailed(exceptions: FirebaseException)
            {
                _userOtpResponse.value =  NetworkResponse.Error(exceptions.message)
            } // onVerificationFailed closed
        } // callback closed

        val options = PhoneAuthOptions.newBuilder(firebaseAuth!!)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setCallbacks(callback)
            .setActivity(activity)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    } // sendFirebaseOtp


    // This will verify the OTP and check if user already exists or not

    val _userVerificationResponse:MutableLiveData<NetworkResponse<Boolean>> = MutableLiveData()
    val userVerificationResponse:LiveData<NetworkResponse<Boolean>> = _userVerificationResponse

    fun verifyUser(verificationId: String?, otpCode: String) = viewModelScope.launch ()
    {

        _userVerificationResponse.value = NetworkResponse.Loading()
        try
        {
            val authCredential = PhoneAuthProvider.getCredential(verificationId!!, otpCode)
            val user = firebaseAuth?.signInWithCredential(authCredential)?.await()?.user
            if ( user!= null)
            {
                // check if user is new or exists already
                val user = firebaseFireStore?.collection(Constants.USER_COLLECTION)
                    ?.document(firebaseAuth?.currentUser?.phoneNumber!!)?.get()?.await()
                if(user!!.exists())
                {
                    _userVerificationResponse.value = NetworkResponse.Success(true)
                }else
                {
                    _userVerificationResponse.value = NetworkResponse.Success(false)
                } // else closed
            } // if closed
        }catch (e:Exception)
        {
            _userVerificationResponse.value = NetworkResponse.Error(e.message)
        } // catch closed
    } // verifyUser closed



    /*** Shared Data b/w register and otp verifcation */

    private val shareData:MutableLiveData<VerificationEntity> = MutableLiveData()
    val getSharedData:LiveData<VerificationEntity> = shareData


    fun setVerificationEntity(verificationEntity: VerificationEntity)
    {
        shareData.value =verificationEntity
    }


} /// AuthViewModel