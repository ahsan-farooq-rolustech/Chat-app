//package com.example.chatapplication.utilities.helperClasses
//
//import com.example.chatapplication.ChatApplication
//import com.example.chatapplication.utilities.utils.FBConstants
//
//class FBStoreHelper {
//
//    private lateinit var mListener: IFBStoreListener
//
//    fun setListener(listener: IFBStoreListener) {
//        mListener = listener
//    }
//
//    fun insertUser(email:String) {
//        val docRef = ChatApplication.firestore.collection(FBConstants.TABLE_USERS).document()
//        val userVal = HashMap<String, Any>()
//        userVal[FBConstants.COL_EMAIL] = email
//
//        //Successfully Inserted Listener, Failure listener can also be handled
//        docRef.set(userVal).addOnSuccessListener {
//            mListener.onUserInsertedSuccessfully()
//        }
//
//    }
//
//    fun getUserProfile() {
//        val docRef = FirebaseApp.fbStore.collection(FBConstants.TABLE_USERS).document(FirebaseApp.fbAuth.currentUser!!.uid)
//        val user = User()
//        user.id = FirebaseApp.fbAuth.currentUser!!.uid
//
//        /*docRef.get().addOnSuccessListener{
//        }*/ //for getting the simple values of table
//
//        //if you want to get the realTimeChange
//        docRef.addSnapshotListener { value, error ->
//            user.firstName = value!!.get(FBConstants.COL_FIRST_NAME).toString()
//            user.lastName = value!!.get(FBConstants.COL_LAST_NAME).toString()
//            user.phoneNumber = value!!.get(FBConstants.COL_PHONE).toString()
//            user.email = value!!.get(FBConstants.COL_EMAIL).toString()
//            mListener.getUserProfile(user)
//        }
//    }
//}