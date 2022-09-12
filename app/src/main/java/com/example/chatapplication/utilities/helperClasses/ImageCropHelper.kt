package com.example.chatapplication.utilities.helperClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 *  To make Helper Class of Crop-Image generic, both Fragments and Activities will be passed as objects.
 *  Along side of boolean status which tell helper class if passed object is Activity or not (is Fragment).
 *  Set Listener of passed Object(Activity/Fragment) to implement overridden methods, to get cropped image as result.
 */
class ImageCropHelper(mObject: Object, isActivity: Boolean) {
    private lateinit var mListener: IImageCropResultListener

    /**
     * This will be initialized when the implementing class will be initialized
     */
    fun setImageCropListener(listener: IImageCropResultListener) {
        mListener = listener
    }

    /**
     * This is Activity Result contract that will be used to register Activity for Result.
     */
    private val uCropImageContract = object : ActivityResultContract<Uri, Uri>() {
        /**
         *  This will process the given Image for cropping. Customization of Cropper UI, default size of Image, etc. can be given here.
         */
        override fun createIntent(context: Context, input: Uri): Intent {
            val outputUri = File(context.filesDir, "${System.currentTimeMillis()}.png").toUri()

            val uCrop = UCrop.of(input, outputUri)
            return uCrop.getIntent(context)
        }

        /**
         *   This will return result obtained after image has been cropped. If image cropper in app
         *   is closed, then it will also return Error.
         */
        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    return UCrop.getOutput(intent)!!
                }
            }
            return "Error".toUri()
        }
    }

    /**
     * This will register uCrop activity to fetch result after image has cropped.
     */
    private val uCropImage = (if (isActivity) mObject as ComponentActivity else mObject as Fragment).registerForActivityResult(uCropImageContract) { uri ->
        if (uri != "Error".toUri()) {
            mListener.onImageCropUriResult(uri)
        } else {
            mListener.onImageCropResultError()
        }
    }

    /**
     * Image Uri will be given to this function and Image Cropping will be obtained from this in onImageCropUriResult.
     */
    fun cropImage(uri: Uri) {
        uCropImage.launch(uri)
    }
}

/**
 * This interface will listened/implemented in calling Activity and methods will be overridden to get result or error (in case).
 */
interface IImageCropResultListener {
    fun onImageCropUriResult(uri: Uri)
    fun onImageCropResultError()
}