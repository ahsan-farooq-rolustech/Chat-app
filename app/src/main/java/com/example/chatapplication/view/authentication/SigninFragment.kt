package com.example.chatapplication.view.authentication

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentSigninBinding
import com.example.chatapplication.utilities.helperClasses.*
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.viewmodel.AuthViewModel


class SignInFragment : Fragment(), View.OnClickListener, IFBAuthListener, IFirestoreListener,TextWatcher,
    IImageResultListener
{
    private lateinit var binding: FragmentSigninBinding
    private lateinit var authHelper: FBAuthHelper
    private lateinit var firestoreHelper: FBStoreHelper
    private var isUserExist = AppConstants.USER_EXISTANCE_UNKNOWN
    private lateinit var authViewModel: AuthViewModel
    private lateinit var email: String
    private lateinit var imageHelper: ImageHelper
    private lateinit var userImage: Uri
    private lateinit var firstName:String
    private lateinit var lastName:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
//        setViewModel()
        setListeners()
        setHelpers()
        return binding.root
    }

//    private fun setViewModel()
//    {
//        val authRepository = AuthRepository(this)
//        authViewModel = ViewModelProvider(
//            this,
//            AuthViewModelFactory(requireActivity().application, authRepository)
//        ).get(AuthViewModel::class.java)
//    }

    private fun setHelpers()
    {
        authHelper = FBAuthHelper(requireContext())
        authHelper.setListener(this)
        firestoreHelper = FBStoreHelper()
        firestoreHelper.setListener(this)
        imageHelper = ImageHelper(this, false)
        imageHelper.setImageListener(this)
    }

    private fun setListeners()
    {
        binding.cvSignin.setOnClickListener(this)
        binding.imvProfile.setOnClickListener(this)
        binding.etEmail.addTextChangedListener(this)

    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.cvSignin ->
            {
                authenticate()
            }

            R.id.imvProfile ->
            {
                imageHelper.getImage(ImageSource.Gallery)
            }
        }
    }

    private fun authenticate()
    {
        isLoading()
        email = binding.etEmail.text.toString()
        val password = binding.etPasword.text.toString()
        val confirmPassword = binding.etConfirmPasword.text.toString()
        firstName=binding.etFirstName.text.toString()
        lastName=binding.etLastName.text.toString()
        if (isUserExist == AppConstants.USER_EXISTANCE_UNKNOWN)
        {

            if (Validators.validateEmail(email))
            {
                firestoreHelper.isUserExists(email)
            }
            else
            {
                binding.etEmail.error = AppAlerts.INCORRECT_EMAIL
                isLoading(false)
            }
        }

        if (isUserExist == AppConstants.USER_DOESNOT_EXISTS)
        {
            if (validateInputs(email, password, confirmPassword))
            {
                authHelper.authenticateUser(email, password, FBConstants.REGISTER)
            }
        }
        if (isUserExist == AppConstants.USER_EXISTS)
        {
            if (validateInputs(email, password, ""))
            {
                authHelper.authenticateUser(email, password, FBConstants.LOGIN)
            }
        }

    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean
    {
        if (!Validators.validateEmail(email))
        {
            binding.etEmail.error = AppAlerts.INCORRECT_EMAIL
            isLoading(false)
            return false
        }

        val validateResult = Validators.validatePassword(password, confirmPassword)
        if (validateResult == ValidationConstants.PASSWORD_LEN_ERROR)
        {
            binding.etPasword.error = AppAlerts.PASSWORD_LENGTH_SHORT
            isLoading(false)
            return false
        }

        if (validateResult == ValidationConstants.PASSWORD_DONOT_MATCH)
        {
            binding.etPasword.error = AppAlerts.PASSWORD_DONOT_MATCH
            binding.etConfirmPasword.error = AppAlerts.PASSWORD_DONOT_MATCH
            isLoading(false)
            return false
        }

        return true
    }

    override fun onLoginError(error: String)
    {
        isLoading(false)
        requireContext().showToastMessage(error)
        Log.d("onLoginError", error)
    }

    override fun onRegistrationError(error: String)
    {
        isLoading(false)
        Log.d("onRegistrationError", error)
        //TODO:show a proper mesage
        requireContext().showToastMessage("registration error")
    }

    override fun onLoginSuccess()
    {
        isLoading(false)
        //TODO:show a proper mesage
        requireContext().showToastMessage("login success")
        val action = SignInFragmentDirections.actionSigninFragmentFragmentToShowAllChatFragment()
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onCompleteRegistration()
    {
        isLoading(false)
        //TODO:show a proper mesage
        requireContext().showToastMessage("registration success")
        firestoreHelper.insertUser(email = email,firstName=firstName, lastName = lastName, imageUri = userImage.toString())
        selectSignInMode()
    }

    private fun isLoading(show: Boolean = true)
    {
        if (show) binding.pbLoader.visibility = View.VISIBLE
        else binding.pbLoader.visibility = View.GONE
    }

    override fun onUserExists()
    {
        isUserExist = AppConstants.USER_EXISTS
        isLoading(false)
        selectSignInMode()
    }

    override fun onUserDoesNotExists()
    {
        isUserExist = AppConstants.USER_DOESNOT_EXISTS
        isLoading(false)
        selectSignUpMode()
    }

    override fun onUserInsertedSuccessfully()
    {
        //TODO:show a proper mesage
        requireContext().showToastMessage("user inserted on firestore")
        selectSignInMode()
    }

    override fun onImageUriResult(uri: List<Uri>, isSingle: Boolean)
    {
        userImage=uri[0]
    }

    private fun selectSignUpMode()
    {
        binding.imvProfile.visibility=View.VISIBLE
        binding.etPasword.visibility=View.VISIBLE
        binding.etConfirmPasword.visibility=View.VISIBLE
        binding.etFirstName.visibility=View.VISIBLE
        binding.etLastName.visibility=View.VISIBLE
        binding.tvButtonText.text=getString(R.string.sign_up)
    }

    private fun selectSignInMode()
    {
        binding.imvProfile.visibility=View.GONE
        binding.etPasword.visibility=View.VISIBLE
        binding.etConfirmPasword.visibility=View.GONE
        binding.etFirstName.visibility=View.GONE
        binding.etLastName.visibility=View.GONE
        binding.tvButtonText.text=getString(R.string.sign_in)
    }

    private fun selectUserUnknownMode()
    {
        binding.etEmail.visibility=View.VISIBLE
        binding.imvProfile.visibility=View.GONE
        binding.etPasword.visibility=View.GONE
        binding.etConfirmPasword.visibility=View.GONE
        binding.etFirstName.visibility=View.GONE
        binding.etLastName.visibility=View.GONE
        binding.tvButtonText.text=getString(R.string.procede)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
    {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
    {
        
    }

    override fun afterTextChanged(s: Editable?)
    {
        if(s.toString().isEmpty())
        {
            selectUserUnknownMode()
        }
    }

}