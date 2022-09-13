package com.example.chatapplication.view.base

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


open class ActivityBase : AppCompatActivity() {

    /**
     * @return the lifecycleState
     */
    /**
     * @param lifecycleState the lifecycleState to set
     */
    var lifecycleState: State? = null

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */


    /**
     * It saves the current state of the application.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    /**
     * Restores the saved application state, in case of be needed, and does default settings for the action bar.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
    }

    override fun onStart() {
        super.onStart()
        lifecycleState = State.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleState = State.RESUMED
    }

    override fun onPause() {
        super.onPause()
        lifecycleState = State.PAUSED
    }


    override fun onStop() {
        super.onStop()
        lifecycleState = State.STOPPED
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleState = State.DESTROYED
    }

    /**
     * The possibles states of an activity lifecycle.
     */
    enum class State {
        CREATED, STARTED, RESUMED, PAUSED, STOPPED, DESTROYED
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(color: Int, flag: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
            activity.window.decorView.systemUiVisibility = flag

        }
    }

    companion object {
        lateinit var activity: AppCompatActivity
    }

    override fun onBackPressed() {

        super.onBackPressed()
    }


}