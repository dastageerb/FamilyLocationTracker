package com.example.familyLocationTracker.views.requirementSetup

import android.app.Application
import androidx.lifecycle.*
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.gpsEnabled
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.locationPermissionsGranted

class RequirementSetupViewModel(application: Application) : AndroidViewModel(application)
{





    private val _hasPermissions:MutableLiveData<Boolean> = MutableLiveData();

    val hasPermission:LiveData<Boolean> = _hasPermissions

    private val _isGpsOn:MutableLiveData<Boolean> = MutableLiveData();


    val isGpsOn:LiveData<Boolean> = _isGpsOn


    fun checkGpsState()
    {
        _isGpsOn.value = getApplication<Application>().applicationContext.gpsEnabled()
    }

    fun checkLocationPermissionsState()
    {
        _hasPermissions.value =getApplication<Application>().applicationContext.locationPermissionsGranted()
    }








}