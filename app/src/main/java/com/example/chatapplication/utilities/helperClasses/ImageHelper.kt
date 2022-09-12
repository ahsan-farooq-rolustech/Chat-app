package com.example.chatapplication.utilities.helperClasses

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageHelper(mObject: Any, isActivity: Boolean) {
    private lateinit var mListener: IImageResultListener

    /**
     * This will be initialized when the implementing class will be initialized
     */
    fun setImageListener(listener: IImageResultListener) {
        mListener = listener
    }

    /**
     *  Get Image will Return Uri of Selected Image. Image Source will be given as Parameter of this function.
     */
    fun getImage(source: ImageSource) {
        if (source == ImageSource.Camera) {
            getCameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } else {
            var selectMultiple = false
            if (source == ImageSource.GalleryMultipleImages) {
                selectMultiple = true
            }

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, selectMultiple)
            intent.type = "image/*"

            getGalleryMultipleResult.launch(intent)
        }
    }

    /**
     * Image Captured from Camera will be sent here in Extras of Intent. Image is present in Bitmap format.
     * To get URI of captured Image, it will save image in
     */
    private var getCameraResult = (if (isActivity) mObject as ComponentActivity else mObject as Fragment).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val receivedIntent: Intent? = result.data
            val bitmapImage = receivedIntent?.extras?.get("data") as Bitmap         //     val capturedImageUri = saveImageAndGetUri((if (isActivity) (mObject as ComponentActivity).applicationContext else (mObject as Fragment).requireContext()), bitmapImage)  // val capturedImageUri =  bitmapToUri( bitmapImage)
            val capturedImageUri = getImageUri((if (isActivity) (mObject as ComponentActivity).applicationContext else (mObject as Fragment).requireContext()), bitmapImage)
            mListener.onImageUriResult(listOf(capturedImageUri))
        } else {
            mListener.onImageResultError()
        }
    }

    /**
     *  This function process the image(s) coming from gallery.
     *  If multiple images are selected, it will return list of URIs of all Images.
     *  Else it will return list with URI of selected single Image.
     */
    private var getGalleryMultipleResult = (if (isActivity) mObject as ComponentActivity else mObject as Fragment).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            if (result.data?.clipData != null) {    // to check if multiple files are selected or not
                val imagesUri = mutableListOf<Uri>()
                val count = result.data!!.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = result.data!!.clipData!!.getItemAt(i).uri
                    imagesUri.add(imageUri)
                }
                mListener.onImageUriResult(imagesUri, false)
            } else { // if single file is selected
                val imageUri = result.data!!.data!!
                mListener.onImageUriResult(listOf(imageUri))
            }
        } else {
            mListener.onImageResultError()
        }
    }

    /**
     * This function will get URI of Bitmap Image
     */
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "capturedImage", null)
        return Uri.parse(path)
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri { //create a file to write bitmap data

        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + System.currentTimeMillis())
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitMapData)
        fos.flush()
        fos.close()
        return file.toUri()
    }

    @Throws(IOException::class) private fun saveImageAndGetUri(ctx: Context, bitmap: Bitmap): Uri {
        val imageUri: Uri
        val imageOutStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "capturedImage.jpg")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.pathSeparator.toString() + "IntermediateApp")
            val uri: Uri = ctx.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            imageUri = uri
            ctx.contentResolver.openOutputStream(uri)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/IntermediateApp"
            val image = File(imagesDir, "capturedImage.jpg")
            imageUri = image.toUri()
            FileOutputStream(image)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream)
        imageOutStream?.close()

        return imageUri
    }


    /**
     * Result will always be in list of Uri even if there is Single Uri in list. 'isSingle' Tells if list is singleton(has one image) or not.
     */
    interface IImageResultListener {
        fun onImageUriResult(uri: List<Uri>, isSingle: Boolean = true)
        fun onImageResultError() {}
    }

    /**
     * This will be used to select source of image.
     */
    enum class ImageSource {
        Camera, Gallery, GalleryMultipleImages
    }
}