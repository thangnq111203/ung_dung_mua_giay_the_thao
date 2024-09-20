package com.fpoly.shoes_app.framework.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ActivityMainBinding
import com.fpoly.shoes_app.framework.presentation.ui.login.LoginScreen
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1001
    private lateinit var audioManager: AudioManager
    private var isBottomNavigationSetup = false
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS
    )
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding? get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupBottomNavigation()
        try {
            audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (!hasPermissions(this, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
            }
            val isDarkModeEnabled = SharedPreferencesManager.isDarkModeEnabled()
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            setupBottomNavigation()
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val tokenFcm = task.result
                SharedPreferencesManager.setToken(tokenFcm)
                Log.d("tokenFcm", tokenFcm)
            })
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (supportFragmentManager.findFragmentById(R.id.loginFragmentScreen) is LoginScreen) {
                        finish() // Exit the app
                    } else {
                        isEnabled = false
                        onBackPressed()
                    }
                }
            })
        } catch (e: IOException) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
        }

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.navBottom)
            .setupWithNavController(navController)
    }

    override fun onStop() {
        super.onStop()
        resetAudioSettings()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun resetAudioSettings() {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_UNMUTE,
            0
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(
                    this, "Bạn cần cấp tất cả các quyền để ứng dụng hoạt động", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    internal fun showBottomNavigation(enable: Boolean) {
        binding?.navBottom?.isVisible = enable
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboard()
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentFocus = currentFocus
        currentFocus?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}