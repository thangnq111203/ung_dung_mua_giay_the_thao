package com.fpoly.shoes_app.framework.presentation.ui.forgot.forGotEmail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.ForgotMailRepository
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMailResponse
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ForGotEmailViewModel
@Inject constructor(
    private val forgotMailRepository: ForgotMailRepository
) : ViewModel() {
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
}