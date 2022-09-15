package com.example.chatapplication.utilities.bindingutils

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.InboxResponseModel
import com.example.chatapplication.utilities.utils.AppConstants
import com.example.chatapplication.utilities.utils.UserPrefConstants
import com.example.chatapplication.utilities.utils.load
import com.example.chatapplication.view.base.ActivityBase

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

        @BindingAdapter("setUserName")
        @JvmStatic
        fun loadUserName(view: TextView, model: InboxResponseModel) {
            if (model.user1Email == ChatApplication.db.getString(UserPrefConstants.EMAIL)) {
                view.text = model.user2Name
            } else {
                view.text = model.user1Name
            }
        }

        @BindingAdapter("setUserImage")
        @JvmStatic
        fun loadUserImage(view: ImageView, model: InboxResponseModel) {
            var imageUrl = ""
            imageUrl = if (model.user1Email == ChatApplication.db.getString(UserPrefConstants.EMAIL)) {
                model.user2ImageUrl
            } else {
                model.user1ImageUrl
            }
            if (!TextUtils.isEmpty(imageUrl)) view.load(imageUrl) else view.setImageResource(R.drawable.ic_user_avatar)
        }

        @BindingAdapter("loadImage")
        @JvmStatic
        fun setImage(view: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) view.load(imageUrl) else view.setImageResource(R.drawable.ic_user_avatar)
        }
    }

}