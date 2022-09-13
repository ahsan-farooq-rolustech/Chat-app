package com.example.chatapplication.utilities.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.chatapplication.view.base.ActivityBase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

val Context.glide: RequestManager?
    get() = Glide.with(this)

fun ImageView.load(path: String) {
    try {
        context.glide!!.load(path).into(this)
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

fun Date.getReadableFormat(): String {
    return SimpleDateFormat("dd MMM,yyyy - hh:mm a", Locale.getDefault()).format(this)
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