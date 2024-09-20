package com.fpoly.shoes_app.framework.presentation.ui.forgot.otpConfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.OTPConfirmReponsitory
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirm
import com.fpoly.shoes_app.framework.domain.model.otpConfirm.OtpConfirmResponse
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class OTPConfirmViewModel
@Inject constructor(
    private val otpConfirmRepository: OTPConfirmReponsitory,
) : ViewModel() {
    private val _otpConfirmResult = MutableStateFlow<Resource<OtpConfirmResponse>>(Resource.init(null))
    val otpConfirmResult: StateFlow<Resource<OtpConfirmResponse>> = _otpConfirmResult

    fun otpConfirm(email: String, otp: String) {
        viewModelScope.launch {
            _otpConfirmResult.value = Resource.loading(null)
            try {
                val response = otpConfirmRepository.otpConfirm(OtpConfirm(email, otp))
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        _otpConfirmResult.value = Resource.success(signUpResponse)
                    } else {
                        _otpConfirmResult.value = Resource.error(null, "Sign-in response is null")
                    }
                } else {
                    _otpConfirmResult.value = Resource.error(null, "Sign-in failed")
                }
            } catch (e: HttpException) {
                _otpConfirmResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _otpConfirmResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}