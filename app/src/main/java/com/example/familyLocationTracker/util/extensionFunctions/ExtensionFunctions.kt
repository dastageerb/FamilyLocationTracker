package com.example.familyLocationTracker.util.extensionFunctions

import android.view.View
import android.widget.TextView

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


    fun TextView.showAnswer(boolean:Boolean)
    {



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