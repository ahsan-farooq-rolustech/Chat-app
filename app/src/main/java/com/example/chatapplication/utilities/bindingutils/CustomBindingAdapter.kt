package com.example.chatapplication.utilities.bindingutils

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.chatapplication.R
import com.example.chatapplication.utilities.utils.AppConstants
import com.example.chatapplication.utilities.utils.load
import com.example.chatapplication.view.MainActivity
import com.example.chatapplication.view.base.ActivityBase
import com.makeramen.roundedimageview.RoundedImageView

class CustomBindingAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter("setUserStatus")
        fun showUserStatus(iv: ImageView, status: Int) {
            when (status) {
                AppConstants.STATUS_ACTIVE -> iv.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                AppConstants.STATUS_IN_ACTIVE -> iv.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.orange))
                AppConstants.STATUS_OFFLINE -> iv.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.error))
                else -> iv.setColorFilter(ContextCompat.getColor(ActivityBase.activity, R.color.error))
            }
        }

        @BindingAdapter("visibleGone")
        @JvmStatic
        fun showHide(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }

        @BindingAdapter("loadImage")
        @JvmStatic
        fun setImage(view: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) view.load(imageUrl)
        }
    }

}