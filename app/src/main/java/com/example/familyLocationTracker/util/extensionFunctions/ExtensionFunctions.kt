package com.example.familyLocationTracker.util.extensionFunctions

import android.view.View

object ExtensionFunctions
{


    fun View.show()
    {
        if(this.visibility == View.GONE)
        {
            this.visibility = View.VISIBLE
        }
    }

    fun View.hide()
    {
        if(this.visibility == View.VISIBLE)
        {
            this.visibility = View.GONE
        }
    }


    fun View.enable()
    {
        if(!this.isEnabled)
        {
            this.isEnabled = true
        }
    }

    fun View.disable()
    {
        if(this.isEnabled)
        {
            this.isEnabled = false
        }
    }




}