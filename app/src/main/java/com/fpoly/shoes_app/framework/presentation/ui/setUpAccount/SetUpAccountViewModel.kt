package com.fpoly.shoes_app.framework.presentation.ui.setUpAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.ForgotMailRepository
import com.fpoly.shoes_app.framework.data.repository.ProfileRepository
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.repository.SetUpAccountRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SetUpAccountViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val setUpAccountRepository: SetUpAccountRepository,
    private val forgotMailRepository: ForgotMailRepository

) : ViewModel() {
    private val _setUpResult = MutableStateFlow<Resource<SetUpAccountResponse>>(Resource.init(null))
    val setUpResult: StateFlow<Resource<SetUpAccountResponse>> = _setUpResult
    private val _findProfileResult = MutableStateFlow<Resource<ProfileResponse>>(Resource.loading(null))
    val findProfileResult: StateFlow<Resource<ProfileResponse>> = _findProfileResult
    private val _forgotMailResult = MutableStateFlow<Resource<ForgotMailResponse>>(Resource.init(null))
    val forgotMailResult: StateFlow<Resource<ForgotMailResponse>> = _forgotMailResult

    fun forgotMail(username: ForgotMail) {
        viewModelScope.launch {
            _forgotMailResult.value = Resource.loading(null)
            try {
                val response = forgotMailRepository.forgotMail(username)
                if (response.isSuccessful) {
                    val setUpResponse = response.body()
                    if (setUpResponse != null) {
                        _forgotMailResult.value = Resource.success(setUpResponse)
                        Log.e("idUser", setUpResponse.userId!!)
                    } else {
                        _forgotMailResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _forgotMailResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _forgotMailResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _forgotMailResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
    fun setUp(id: String, imageFile: File?, phoneNumber: String?, fullName: String?, nameAccount: String?, birthday: String?, grender: String?) {
        viewModelScope.launch {
            _setUpResult.value = Resource.loading(null)
            try {
                val response = setUpAccountRepository.setUpAccount(
                    id, imageFile, phoneNumber, fullName, nameAccount, birthday, grender
                )
                if (response.isSuccessful) {
                    _setUpResult.value = Resource.success(response.body())
                } else {
                    _setUpResult.value = Resource.error(null, "Set-up failed")
                }
            }  catch (e: HttpException) {
                _setUpResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _setUpResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
    fun profilefind(id: String) {
        viewModelScope.launch {
            _findProfileResult.value = Resource.loading(null)
            try {
                val response = profileRepository.profile(id)
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    if (profileResponse != null) {
                        _findProfileResult.value = Resource.success(profileResponse)
                    } else {
                        _findProfileResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _findProfileResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _findProfileResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _findProfileResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }

}