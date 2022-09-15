package com.example.chatapplication.view.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.chatapplication.R
import com.example.chatapplication.data.responseModel.UserResponseModel
import com.example.chatapplication.databinding.FragmentUsersBinding
import com.example.chatapplication.view.chat.adapter.UserAdapter
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.AppAlerts
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.IUserListener
import com.example.chatapplication.view.base.ActivityBase

class UsersFragment : Fragment(), View.OnClickListener, IFirestoreListener, IUserListener {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var fsHelper: FBStoreHelper
    private lateinit var adapter: UserAdapter
    private lateinit var usersList: ArrayList<UserResponseModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        setListeners()
        setHelpers()
        getUsers()
        return binding.root
    }

    private fun setListeners() {
        binding.imvBack.setOnClickListener(this)
    }

    private fun setHelpers() {
        fsHelper = FBStoreHelper()
        fsHelper.setListener(this)
    }

    private fun getUsers() {
        loading(true)
        fsHelper.getUsers()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imvBack -> {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoader.visibility = View.VISIBLE
        } else {
            binding.pbLoader.visibility = View.GONE
        }
    }

    private fun showErrorMessage() {
        binding.tvErrorMessage.text = AppAlerts.NO_USER_AVAILABLE
        binding.tvErrorMessage.visibility = View.VISIBLE
    }


    override fun onUserGetSuccessfully(userResponsModels: ArrayList<UserResponseModel>) {
        setAdapter(userResponsModels)
        binding.rvUsers.visibility = View.VISIBLE
        loading(false)
        getChangesInUser()
    }

    private fun getChangesInUser() {
        fsHelper.getUserChanges()
    }

    private fun setAdapter(userResponsModels: ArrayList<UserResponseModel>) {
        usersList = userResponsModels
        adapter = UserAdapter(ActivityBase.activity, usersList)
        adapter.setListener(this)
        binding.rvUsers.adapter = adapter
        adapter.notifyItemRangeChanged(0, usersList.size)
    }

    override fun onUserDoesNotExists() {
        showErrorMessage()
    }

    override fun onUserGetFailure(error: String) {
        showErrorMessage()
        Log.d("onUserGetFailure", error)
    }

    override fun onUserClicked(userResponseModel: UserResponseModel) {
        val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(userResponseModel)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onGetUserChangesSuccessful(userResponseModels: ArrayList<UserResponseModel>) {
        userResponseModels.removeAll(usersList.toSet())
        for (i in userResponseModels) {
            for (j in 0 until usersList.size) {
                if (i.email == usersList[j].email) {
                    usersList[j] = i
                    break
                }
            }
        }
        adapter.notifyItemRangeChanged(0, usersList.size)
    }
}