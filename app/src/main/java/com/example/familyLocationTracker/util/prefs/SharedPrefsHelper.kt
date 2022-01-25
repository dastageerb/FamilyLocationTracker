package com.example.familyLocationTracker.util.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build.USER
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants
import com.google.gson.Gson

class SharedPrefsHelper(private val context: Context)
{


    private var  prefs: SharedPreferences?=null
    private var editor: SharedPreferences.Editor?=null
    init
    {
        prefs  =context.getSharedPreferences(Constants.Shared_PREFS, Context.MODE_PRIVATE)
        editor = prefs?.edit()!!;
    }
    /** USER CRUDS ***/

    fun saveUser(data: User?)
    {
        val user = Gson().toJson(data)
        editor?.putString(USER, user)
        editor?.commit()
    }

    fun clearUser()
    {
        editor?.putString(USER,"")
        editor?.commit()
    }


    fun getUser(): User?
    {
        val data = prefs?.getString(USER,"")
        return Gson().fromJson(data,User::class.java)
    }



}