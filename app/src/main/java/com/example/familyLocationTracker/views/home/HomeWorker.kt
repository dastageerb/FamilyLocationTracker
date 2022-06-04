package com.example.familyLocationTracker.views.home

import android.content.Context
import android.content.ContextParams
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.uploadUserLocation
import timber.log.Timber
import java.lang.Exception

class HomeWorker(val context:Context,params:WorkerParameters) : CoroutineWorker(context,params)
{


    override suspend fun doWork(): Result
    {



        Timber.tag(Constants.TAG).d("worker Working")
        context.uploadUserLocation()

        return Result.success()
    } // do work closed


} // HomeWorker closed