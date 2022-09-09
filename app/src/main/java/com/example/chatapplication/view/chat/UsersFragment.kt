package com.example.chatapplication.view.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.chatapplication.R
import com.example.chatapplication.data.response.UserResponse
import com.example.chatapplication.databinding.FragmentUsersBinding
import com.example.chatapplication.view.chat.adapter.UserChatAdapter
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.utils.AppAlerts
import com.example.chatapplication.utilities.utils.IFirestoreListener
import com.example.chatapplication.utilities.utils.IUserListener
import com.example.chatapplication.view.MainActivity

class UsersFragment : Fragment(),View.OnClickListener,IFirestoreListener,IUserListener
{
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    companion object
//    {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment UsersFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic fun newInstance(param1: String, param2: String) = UsersFragment().apply {
//            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
    private lateinit var binding: FragmentUsersBinding
    private lateinit var firestoreHelper:FBStoreHelper
    private lateinit var adapter:UserChatAdapter
    private lateinit var usersList:ArrayList<UserResponse>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding=FragmentUsersBinding.inflate(layoutInflater,container,false)
        setListeners()
        setHelpers()
        getUsers()
        return binding.root
    }

    private fun setListeners()
    {
        binding.imvBack.setOnClickListener(this)
    }

    private fun setHelpers()
    {
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
    }

    private fun getUsers()
    {
        loading(true)
        firestoreHelper.getUsers()
    }


    override fun onClick(v: View?)
    {
        when(v?.id)
        {
            R.id.imvBack->{
                requireActivity().onBackPressed()
            }
        }
    }

    private fun loading(isLoading:Boolean)
    {
        if(isLoading)
        {
            binding.pbLoader.visibility=View.VISIBLE
        }
        else
        {
            binding.pbLoader.visibility=View.GONE
        }
    }

    private fun showErrorMessage()
    {
        binding.tvErrorMessage.text=AppAlerts.NO_USER_AVALABLE
        binding.tvErrorMessage.visibility=View.VISIBLE
    }


    override fun onUserGetSuccessfully(userResponses: ArrayList<UserResponse>)
    {
        setAdapter(userResponses)
        binding.rvUsers.visibility=View.VISIBLE
        loading(false)
        getChangesInUser()
    }

    private fun getChangesInUser()
    {
        firestoreHelper.getUserChanges()
    }

    private fun setAdapter(userResponses: ArrayList<UserResponse>)
    {
        usersList=userResponses
        adapter= UserChatAdapter(MainActivity.mActivity,usersList)
        adapter.setListener(this)
        binding.rvUsers.adapter=adapter
        adapter.notifyDataSetChanged()
    }
    override fun onUserDoesNotExists()
    {
        showErrorMessage()
    }

    override fun onUserGetFailure(error: String)
    {
        showErrorMessage()
        Log.d("onUserGetFailure", error)
    }

    override fun onUserClicked(userResponse: UserResponse)
    {
        val action=UsersFragmentDirections.actionUsersFragmentToChatFragment(userResponse)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onGetUserChangesSuccessful(userResponses: ArrayList<UserResponse>)
    {
        userResponses.removeAll(usersList.toSet())
        for( i in userResponses)
        {
            for(j in 0 until usersList.size)
            {
                if(i.email==usersList[j].email)
                {
                    usersList[j]=i
                    break
                }
            }
        }
        adapter.notifyDataSetChanged()
    }


}