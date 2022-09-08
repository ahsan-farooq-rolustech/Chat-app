package com.example.chatapplication.utilities.bindingutils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.chatapplication.R
import com.example.chatapplication.utilities.utils.AppConstants
import com.example.chatapplication.view.MainActivity

class CustomBindingAdapter
{
    companion object {

        @JvmStatic
        @BindingAdapter("setUserStatus")
        fun showUserStatus(iv: ImageView, status: Int)
        {
            when(status){
                AppConstants.STATUS_ACTIVE -> iv.setColorFilter(ContextCompat.getColor(MainActivity.mActivity, R.color.green))
                AppConstants.STATUS_IN_ACTIVE -> iv.setColorFilter(ContextCompat.getColor(MainActivity.mActivity, R.color.orange))
                AppConstants.STATUS_OFFLINE -> iv.setColorFilter(ContextCompat.getColor(MainActivity.mActivity, R.color.error))
                else ->  iv.setColorFilter(ContextCompat.getColor(MainActivity.mActivity, R.color.error))
            }
        }
    }

}