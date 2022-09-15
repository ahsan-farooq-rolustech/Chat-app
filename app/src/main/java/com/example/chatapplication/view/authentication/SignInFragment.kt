package com.example.chatapplication.view.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapplication.ChatApplication
import com.example.chatapplication.R
import com.example.chatapplication.databinding.FragmentSigninBinding
import com.example.chatapplication.utilities.helperClasses.FBAuthHelper
import com.example.chatapplication.utilities.helperClasses.FBStoreHelper
import com.example.chatapplication.utilities.helperClasses.ImageHelper
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.view.MainActivity
import com.example.chatapplication.view.base.ActivityBase


class SignInFragment : Fragment(), View.OnClickListener, IFBAuthListener, IFirestoreListener, TextWatcher, ImageHelper.IImageResultListener {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var authHelper: FBAuthHelper
    private lateinit var fsHelper: FBStoreHelper
    private var userStatus = AppConstants.MODE_INITIAL_STATE
    private lateinit var email: String
    private lateinit var imageHelper: ImageHelper
    private lateinit var userImage: Uri
    private lateinit var firstName: String
    private lateinit var lastName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        setListeners()
        initThings()
        return binding.root
    }

    private fun initThings() {
        binding.progress = ""
        binding.isSignUp = false
        binding.isLogin = false
        binding.isLoading = false
        authHelper = FBAuthHelper(ActivityBase.activity)
        authHelper.setListener(this)
        fsHelper = FBStoreHelper()
        fsHelper.setListener(this)
        imageHelper = ImageHelper(this, false)
        imageHelper.setImageListener(this)
    }

    private fun setListeners() {
        binding.cvSignin.setOnClickListener(this)
        binding.imvProfile.setOnClickListener(this)
        binding.etEmail.addTextChangedListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvSignin -> {
                authenticate()
            }

            R.id.imvProfile -> {
                imageHelper.getImage(ImageHelper.ImageSource.Gallery)
            }
        }
    }

    private fun authenticate() {

        binding.isLoading = true
        email = binding.etEmail.text.toString()
        val password = binding.etPasword.text.toString()
        val confirmPassword = binding.etConfirmPasword.text.toString()
        firstName = binding.etFirstName.text.toString()
        lastName = binding.etLastName.text.toString()

        when (userStatus) {
            AppConstants.MODE_INITIAL_STATE -> {
                checkIfUserExists()
            }
            AppConstants.MODE_SIGN_IN -> {
                signIn(email, password)
            }
            AppConstants.MODE_SIGN_UP -> {
                signUp(email, password, confirmPassword, firstName, lastName)
            }
        }
    }

    private fun signUp(email: String, password: String, confirmPassword: String, firstName: String, lastName: String) {
        if (validateInputs(email, password, confirmPassword, firstName, lastName)) {
            authHelper.authenticateUser(email, password, FBConstants.REGISTER)
        }
    }

    private fun signIn(email: String, password: String) {
        if (validateInputs(email, password, "")) {
            authHelper.authenticateUser(this.email, password, FBConstants.LOGIN)
        }
    }

    private fun checkIfUserExists() {
        if (Validators.validateEmail(email)) {
            fsHelper.isUserExists(email)
        } else {
            binding.etEmail.error = AppAlerts.INCORRECT_EMAIL
            binding.isLoading = false
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String, firstName: String = "-1", lastName: String = "-1"): Boolean {
        if (userStatus == AppConstants.MODE_SIGN_UP && !this::userImage.isInitialized) {
            ActivityBase.activity.showToastMessage(AppAlerts.INSERT_USER_IMAGE)
            return false

        }
        if (!Validators.validateEmail(email)) {
            binding.etEmail.error = AppAlerts.INCORRECT_EMAIL
            binding.isLoading = false
            return false
        }

        val validateResult = Validators.validatePassword(password, confirmPassword)
        if (validateResult == ValidationConstants.PASSWORD_LEN_ERROR) {
            binding.etPasword.error = AppAlerts.PASSWORD_LENGTH_SHORT
            binding.isLoading = false
            return false
        }

        if (validateResult == ValidationConstants.PASSWORD_NOT_MATCH) {
            binding.etPasword.error = AppAlerts.PASSWORD_NOT_MATCH
            binding.etConfirmPasword.error = AppAlerts.PASSWORD_NOT_MATCH
            binding.isLoading = false
            return false
        }

        if (firstName.isEmpty()) {
            binding.etFirstName.error = AppAlerts.FIRST_NAME_ERROR
            binding.isLoading = false
            return false
        }

        if (lastName.isEmpty()) {
            binding.etLastName.error = AppAlerts.LAST_NAME_ERROR
            binding.isLoading = false
            return false
        }
        return true
    }

    override fun onLoginError(error: String) {
        binding.isLoading = false
        ActivityBase.activity.showToastMessage(error)
        Log.d("onLoginError", error)
    }

    override fun onRegistrationError(error: String) {
        binding.isLoading = false
        Log.d("onRegistrationError", error)
        ActivityBase.activity.showToastMessage(AppAlerts.REGISTRATION_ERROR)
    }

    override fun onLoginSuccess() {
        binding.isLoading = false
        ActivityBase.activity.showToastMessage(AppAlerts.LOGIN_SUCCESS)
        goToMainActivity()
    }

    private fun goToMainActivity() {
        setUserPreferences()
        ActivityBase.activity.apply {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    override fun onCompleteRegistration() {
        binding.isLoading = false
        ActivityBase.activity.showToastMessage(AppAlerts.REGISTRATION_SUCCESS)
        fsHelper.uploadImageToStore(userImage)
    }

    override fun onUserExists() {
        userStatus = AppConstants.MODE_SIGN_IN
        binding.isLoading = false
        setViews(AppConstants.MODE_SIGN_IN)
    }

    override fun onUserDoesNotExists() {
        userStatus = AppConstants.MODE_SIGN_UP
        binding.isLoading = false
        setViews(AppConstants.MODE_SIGN_UP)
    }

    override fun onUserInsertedSuccessfully() {
        ActivityBase.activity.showToastMessage(AppAlerts.USER_INSERTED_SUCCESS)
        setViews(AppConstants.MODE_SIGN_IN)
        binding.isLoading = false
        goToMainActivity()
    }

    private fun setUserPreferences() {
        ChatApplication.db.putString(UserPrefConstants.FULL_NAME, "${binding.etFirstName.text} ${binding.etLastName.text}")
        ChatApplication.db.putString(UserPrefConstants.EMAIL, "${binding.etEmail.text}")
    }

    override fun onImageUriResult(uri: List<Uri>, isSingle: Boolean) {
        userImage = uri[0]
        binding.imvProfile.setImageURI(userImage)
    }

    private fun setViews(type: Int) {
        when (type) {
            AppConstants.MODE_SIGN_IN -> {
                binding.isLogin = true
                binding.isSignUp = false
                userStatus = AppConstants.MODE_SIGN_IN
            }
            AppConstants.MODE_SIGN_UP -> {
                binding.isLogin = false
                binding.isSignUp = true
                userStatus = AppConstants.MODE_SIGN_UP
            }
            AppConstants.MODE_INITIAL_STATE -> {
                binding.isLogin = false
                binding.isSignUp = false
                userStatus = AppConstants.MODE_INITIAL_STATE
            }

        }
    }

    override fun onUserImageUploadedSuccessfully(imageUrl: String) {
        fsHelper.insertUser(email = email, firstName = firstName, lastName = lastName, imageUrl = imageUrl)
    }

    override fun onUserImageUploadProgress(progress: Int) {
        binding.progress = "$progress"
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        if (s.toString().isEmpty()) {
            setViews(AppConstants.MODE_INITIAL_STATE)
        }
    }

}