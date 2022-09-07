package com.example.chatapplication.view.authentication

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.chatapplication.R
import com.example.chatapplication.data.repository.AuthRepository
import com.example.chatapplication.databinding.FragmentSigninBinding
import com.example.chatapplication.utilities.helperClasses.*
import com.example.chatapplication.utilities.utils.*
import com.example.chatapplication.viewmodel.AuthViewModel
import com.example.chatapplication.viewmodel.viewModelFactory.AuthViewModelFactory
import java.io.ByteArrayOutputStream


class SignInFragment : Fragment(), View.OnClickListener, IFBAuthListener, IFirestoreListinner,
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
        showCircularProgress()
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
            return false
        }

        val validateResult = Validators.validatePassword(password, confirmPassword)
        if (validateResult == ValidationConstants.PASSWORD_LEN_ERROR)
        {
            binding.etPasword.error = AppAlerts.PASSWORD_LENGTH_SHORT
            return false
        }

        if (validateResult == ValidationConstants.PASSWORD_DONOT_MATCH)
        {
            binding.etPasword.error = AppAlerts.PASSWORD_DONOT_MATCH
            binding.etConfirmPasword.error = AppAlerts.PASSWORD_DONOT_MATCH

            return false
        }

        return true
    }

    override fun onLoginError(error: String)
    {
        showCircularProgress(false)
        requireContext().showToastMessage(error)
        Log.d("onLoginError", error)

    }

    override fun onRegistrationError(error: String)
    {
        showCircularProgress(false)
        Log.d("onRegistrationError", error)
        requireContext().showToastMessage("registration error")
    }

    override fun onLoginSuccess()
    {
        showCircularProgress(false)
        requireContext().showToastMessage("login success")
        val action = SignInFragmentDirections.actionSigninFragmentFragmentToShowAllChatFragment()
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onCompleteRegistration()
    {
        showCircularProgress(false)
        requireContext().showToastMessage("registration success")
        firestoreHelper.insertUser(email = email,firstName=firstName, lastName = lastName, imageUri = userImage.toString())
        selectSignInMode()
    }

    private fun showCircularProgress(show: Boolean = true)
    {
        if (show) binding.pbLoader.visibility = View.VISIBLE
        else binding.pbLoader.visibility = View.GONE
    }
//    private fun showCircularProgress(false)
//    {
//        binding.pbLoader.visibility=View.GONE
//    }

    override fun onUserExists()
    {
        isUserExist = AppConstants.USER_EXISTS
//        binding.etPasword.visibility = View.VISIBLE
        showCircularProgress(false)
        selectSignInMode()
//        binding.tvButtonText.text = getString(R.string.sign_in)
    }

    override fun onUserDoesNotExists()
    {
//        binding.etPasword.visibility = View.VISIBLE
//        binding.etConfirmPasword.visibility = View.VISIBLE
        isUserExist = AppConstants.USER_DOESNOT_EXISTS
        showCircularProgress(false)
        selectSignUpMode()
//        binding.tvButtonText.text = getString(R.string.sign_up)
    }

    override fun onUserInsertedSuccessfully()
    {
        requireContext().showToastMessage("user inserted on firestore")
    }

//    private fun encodedImage(bitmap: Bitmap): String?
//    {
//        val previewWidth = 150
//        val previewHeight = bitmap.height * previewWidth / bitmap.width
//        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
//        val byteOutputStream = ByteArrayOutputStream()
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteOutputStream)
//        val bytes = byteOutputStream.toByteArray()
//        return Base64.encodeToString(bytes, Base64.DEFAULT)
//    }

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

}