package com.example.familyLocationTracker.views.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.models.user.UserLocation
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.prefs.SharedPrefsHelper
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.type.Date
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

class SetupProfileViewModel(application: Application):AndroidViewModel(application)
{


    private var sharedPrefsHelper: SharedPrefsHelper?=null
    init
    {
        sharedPrefsHelper = SharedPrefsHelper(application.applicationContext)
    }


    private val _userUploadResponse:MutableLiveData<NetworkResponse<String>> = MutableLiveData()
    val userUploadResponse:LiveData<NetworkResponse<String>> = _userUploadResponse


    fun uploadUser(imageUri:Uri,name:String,contact:String,city:String,currentLocation:LatLng) = viewModelScope.launch()
    {

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageReference = firebaseStorage.reference
        val firebaseFirestore = FirebaseFirestore.getInstance()
        _userUploadResponse.value = NetworkResponse.Loading()
       try
       {
           val imageRef: StorageReference = storageReference.child(Constants.PROFILE_IMAGES).child(firebaseAuth.currentUser?.phoneNumber!!)
            imageRef.putFile(imageUri).await()
           val imageUrl = imageRef.downloadUrl.await()

           val token = FirebaseMessaging.getInstance().token.await()

           val user = User(name,contact,city,imageUrl.toString(), UserLocation(currentLocation.latitude,currentLocation.longitude),token)


           firebaseFirestore.collection(Constants.USER_COLLECTION)
               .document(firebaseAuth.currentUser!!.phoneNumber!!).set(user).addOnCompleteListener(OnCompleteListener
               {
                   if(it.isSuccessful)
                   {
                        saveUser(user)
                       _userUploadResponse.value = NetworkResponse.Success("User Uploaded SuccessFully")
                   }else
                   {
                       Timber.tag(TAG).d(it.exception?.message)
                       _userUploadResponse.value = NetworkResponse.Error(it.exception?.message)
                   }
               })
       } catch (e:Exception)
       {
           Timber.tag(TAG).d("e  "+e.message)
           _userUploadResponse.value = NetworkResponse.Error(e.message)
       }






//        imageRef.putFile(imageUri).addOnSuccessListener(OnSuccessListener<Any?> {
//            imageRef.getDownloadUrl()
//                .addOnSuccessListener(OnSuccessListener<Uri> { uri -> callBack.onImageUpdatedSuccess(uri.toString()) } // onSuccess closed
//                )
//            imageRef.getDownloadUrl()
//                .addOnFailureListener(OnFailureListener { e -> callBack.onImageUpdateFailure(e.message) })
//        })

    } // uploadUser closed



    // saved Prefs

    fun getUser() = sharedPrefsHelper?.getUser()

    fun saveUser(user: User) = sharedPrefsHelper?.saveUser(user)

    fun deleteUser() = sharedPrefsHelper?.clearUser()



}