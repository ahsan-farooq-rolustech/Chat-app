package com.example.chatapplication.utilities.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.utilities.glideModules.GlideApp
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

val Context.glide: RequestManager?
    get() = GlideApp.with(this)

fun ImageView.load(path: String) {
    try {
        val imageReference = ChatApplication.firebaseStorage.getReferenceFromUrl(path)
        context.glide!!.load(imageReference).into(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.getBitmapFromEncodedString(): Bitmap? {
    val bytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.encodeToString(): String? {
    val previewWidth = 150
    val previewHeight = this.height * previewWidth / this.width
    val previewBitmap = Bitmap.createScaledBitmap(this, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val bytes = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}