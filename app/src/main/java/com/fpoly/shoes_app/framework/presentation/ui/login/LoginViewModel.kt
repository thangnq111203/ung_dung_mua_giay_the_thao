// LoginViewModel.kt
package com.fpoly.shoes_app.framework.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.LoginRepository
import com.fpoly.shoes_app.framework.domain.model.login.Login
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {
    private val _loginResult = MutableStateFlow<Resource<LoginResponse>>(Resource.init(null))
    val loginResult: StateFlow<Resource<LoginResponse>> = _loginResult

    fun signIn(username: String, password: String,token:String) {
        viewModelScope.launch {
            _loginResult.value = Resource.loading(null)
            try {
                val response = loginRepository.signIn(Login(username, password,token))
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        _loginResult.value = Resource.success(signUpResponse)
                    } else {
                        _loginResult.value = Resource.error(null, "Sign-in response is null")
                    }
                } else {
                    _loginResult.value = Resource.error(null, "Sign-in failed")
                }
            } catch (e: HttpException) {
                _loginResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _loginResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}


