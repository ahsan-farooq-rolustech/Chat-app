package com.example.chatapplication.view.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.data.repository.AuthRepository
import com.example.chatapplication.databinding.FragmentSigninBinding
import com.example.chatapplication.utilities.helperClasses.FBAuthHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.viewmodel.AuthViewModel
import com.example.chatapplication.viewmodel.viewModelFactory.AuthViewModelFactory


class SignInFragment : Fragment(), View.OnClickListener, IFBAuthListener
{
    private lateinit var binding: FragmentSigninBinding
    private lateinit var authHelper: FBAuthHelper
    private var isUserExist=true
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        setViewModel()
        setListinners()
        setHelpers()

        return binding.root
    }

    private fun setViewModel()
    {
        val authRepository=AuthRepository(this)
        authViewModel=ViewModelProvider(this,AuthViewModelFactory(requireActivity().application,authRepository)).get(AuthViewModel::class.java)
    }

    private fun setHelpers()
    {
        authHelper = FBAuthHelper(requireContext())
        authHelper.setListener(this)
    }

    private fun setListinners()
    {
        binding.cvSignin.setOnClickListener (this)
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.cvSignin ->
            {
                authenticate()
            }
        }
    }

    private fun authenticate()
    {
        showCircularProgress()
        authViewModel.checkIfUserExists(binding.etEmail.text.toString())
//        val email = binding.etEmail.text.toString()
//        val password = binding.etPasword.text.toString()
//        if(!isUserExist)
//        {
//            val confirmPassword=binding.etConfirmPasword.text.toString()
//            if(!validateInputs(email,password,confirmPassword))
//            {
//                hideCircularProgress()
//                return
//            }
//            authHelper.authenticateUser(email,password,FBConstants.REGISTER)
//            return
//        }
//        if(!validateInputs(email,password,""))
//        {
//            hideCircularProgress()
//            return
//        }
//        authHelper.authenticateUser(email, password, FBConstants.LOGIN)

    }

    private fun validateInputs(email: String, password: String, confirmPassword: String):Boolean
    {
        if(!Validators.validateEmail(email))
        {
            binding.etEmail.error=AppAlerts.INCORRECT_EMAIL
            return false
        }

        val validateResult=Validators.validatePassword(password,confirmPassword)
        if(validateResult==ValidationConstants.PASSWORD_LEN_ERROR)
        {
            binding.etPasword.error=AppAlerts.PASSWORD_LENGTH_SHORT
            return false
        }

        if(validateResult==ValidationConstants.PASSWORD_DONOT_MATCH)
        {
            binding.etPasword.error=AppAlerts.PASSWORD_DONOT_MATCH
            binding.etConfirmPasword.error=AppAlerts.PASSWORD_DONOT_MATCH

            return false
        }

        return true
    }

    override fun onLoginError(error: String)
    {
        hideCircularProgress()
        requireContext().showToastMessage(error)
        Log.d("onLoginError",error)
        if(error==AppAlerts.NO_USER_EXCEPTION)
        {
            binding.etConfirmPasword.visibility=View.VISIBLE
            isUserExist=false
        }
    }

    override fun onRegistrationError(error: String)
    {
        hideCircularProgress()
        requireContext().showToastMessage("registration error")
    }

    override fun onCompleteLogin()
    {
        hideCircularProgress()
        requireContext().showToastMessage("login success")
        super.onCompleteLogin()
    }

    override fun onCompleteRegistration()
    {
        hideCircularProgress()
        requireContext().showToastMessage("registration success")
        super.onCompleteRegistration()
    }

    private fun showCircularProgress()
    {
        binding.pbLoader.visibility=View.VISIBLE
    }
    private fun hideCircularProgress()
    {
        binding.pbLoader.visibility=View.GONE
    }

    override fun onUserExists()
    {
        binding.etPasword.visibility=View.VISIBLE
    }



}