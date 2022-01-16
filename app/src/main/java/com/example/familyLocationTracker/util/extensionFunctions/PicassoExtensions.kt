package com.example.familyLocationTracker.util.extensionFunctions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.familyLocationTracker.R
import com.squareup.picasso.Picasso

object PicassoExtensions
{

    fun Context.showToast(msg:String) = Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()








    fun ImageView.load(url:String?)
    {
        if(url!!.isNotEmpty())
        {
            Picasso.get().load(url).into(this)
        }
    }


    fun ImageView.load(url: String?, placeHolder: Int)
    {
        if(url!!.isNotEmpty())
        {
            Picasso.get().load(url).placeholder(placeHolder).into(this)
        }
    }



}