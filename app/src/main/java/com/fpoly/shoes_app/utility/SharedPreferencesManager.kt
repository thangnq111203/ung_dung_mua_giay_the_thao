package com.fpoly.shoes_app.utility

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import javax.inject.Singleton

@Singleton
object SharedPreferencesManager {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: Editor
    lateinit var bundle: Bundle

    private const val SPLASH_SCREEN_NOT_SHOW = "splash_screen_not_show"
    private const val VIBRATE_MODE_KEY = "vibrate_mode"
    private const val SOUND_MODE_KEY = "sound_mode"
    private const val NOTIFICATION_MODE_KEY = "notification_mode"
    private const val userName = "username"
    private const val userNameWait = "userNameWait"
    private const val passWord = "password"
    private const val idUser = "idUser"
    private const val tokenKey = "tokenKey"
    private const val DARK_MODE_KEY = "dark_mode"

    fun isSplashScreenSkipped(): Boolean = getBooleanDataByKey(SPLASH_SCREEN_NOT_SHOW)
    fun isDarkModeEnabled(): Boolean = getBooleanDataByKey(DARK_MODE_KEY)
    fun saveDarkModeState(isDarkMode: Boolean) {
        saveBooleanDataByKey(DARK_MODE_KEY, isDarkMode)
    }

    fun setSplashScreenSkipped(isSkipped: Boolean) {
        saveBooleanDataByKey(SPLASH_SCREEN_NOT_SHOW, isSkipped)
    }
    fun getVibrateModeState(): Boolean = getBooleanDataByKey(VIBRATE_MODE_KEY)
    fun getSoundModeState(): Boolean = getBooleanDataByKey(SOUND_MODE_KEY)
    fun getNotificationModeState(): Boolean = getBooleanDataByKey(NOTIFICATION_MODE_KEY)
    fun saveVibrateModeState(isVibrateMode: Boolean) {
        saveBooleanDataByKey(VIBRATE_MODE_KEY, isVibrateMode)
    }
    fun saveSoundModeState(isSoundMode: Boolean) {
        saveBooleanDataByKey(SOUND_MODE_KEY, isSoundMode)
    }
    fun saveNotificationModeState(isNotificationMode: Boolean) {
        saveBooleanDataByKey(NOTIFICATION_MODE_KEY, isNotificationMode)
    }
    fun getUserName(): String = getStringDataByKey(userName)

    fun getUserNameWait(): String = getStringDataByKey(userNameWait)

    fun setPassWord(email: String, pass: String?) {
        saveStringDataByKey(userName, email)
        saveStringDataByKey(passWord, pass)
    }

    fun setUserWait() {
        saveStringDataByKey(userNameWait, getUserName())
    }

    fun getPassWord(): String = getStringDataByKey(passWord)
    fun removeUser() {
        editor.remove(userName).apply()
        editor.remove(passWord).apply()
    }

    fun setIdUser(id: String?) {
        saveStringDataByKey(idUser, id)
    }

    fun getIdUser(): String = getStringDataByKey(idUser)
    fun setToken(token: String?) {
        saveStringDataByKey(tokenKey, token)
    }

    fun getToken(): String = getStringDataByKey(tokenKey)
    fun removeIdUser() {
        editor.remove(idUser).apply()
    }

    fun removeUserWait() {
        editor.remove(userNameWait).apply()
    }

    private fun saveStringDataByKey(key: String?, data: String?) {
        editor.putString(key, data).apply()
    }

    private fun getStringDataByKey(key: String?): String =
        sharedPreferences.getString(key, "") ?: ""

    private fun saveBooleanDataByKey(key: String?, data: Boolean) {
        editor.putBoolean(key, data).apply()
    }

    private fun getBooleanDataByKey(key: String?): Boolean =
        sharedPreferences.getBoolean(key, false)
}