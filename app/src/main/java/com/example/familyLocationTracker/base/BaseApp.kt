package com.example.familyLocationTracker.base

import android.app.Application
import com.example.familyLocationTracker.BuildConfig
import timber.log.Timber

class BaseApp : Application()
{

    override fun onCreate()
    {
        super.onCreate()

        if (BuildConfig.DEBUG)
        {
            Timber.plant(Timber.DebugTree())
        }


    }

}