package com.example.familyLocationTracker.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContract.SynchronousResult
import androidx.annotation.CallSuper
import com.theartofdev.edmodo.cropper.CropImage

object ImageUtils
{










    val cropImage = object: ActivityResultContract<Uri?, Uri?>()
    {
        override fun createIntent(context: Context, input: Uri?): Intent
        {
            return CropImage.activity(input)
                .setAspectRatio(4,3)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(75)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri?
        {
            return CropImage.getActivityResult(intent)?.uri
        }


    } // pickImageFromGalleryClosed



}