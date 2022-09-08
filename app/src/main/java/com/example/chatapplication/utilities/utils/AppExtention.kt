package com.example.chatapplication.utilities.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToastMessage(message:String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.getBitmapFromEncodedString(): Bitmap?
{
    val bytes=Base64.decode(this,Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
}

fun Date.getReadableFormat():String
{
    return SimpleDateFormat("dd MMM,yyyy - hh:mm a",Locale.getDefault()).format(this)
}
