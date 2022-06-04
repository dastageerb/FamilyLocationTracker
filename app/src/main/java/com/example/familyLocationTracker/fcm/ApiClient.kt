package com.example.familyLocationTracker.fcm

import com.example.familyLocationTracker.util.Constants
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit




object ApiClient
{


    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit?
    {
        if (retrofit == null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.POST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }


    val api = getClient()?.create(ApiInterface::class.java)

}