package com.fpoly.shoes_app.framework.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.ProfileRepository
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.repository.SetUpAccountRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val setUpAccountRepository: SetUpAccountRepository,

    ) : ViewModel() {
    private val _findProfileResult = MutableStateFlow<Resource<ProfileResponse>>(Resource.loading(null))
    val findProfileResult: StateFlow<Resource<ProfileResponse>> = _findProfileResult
    private val _setUpResult = MutableStateFlow<Resource<SetUpAccountResponse>>(Resource.init(null))
    val setUpResult: StateFlow<Resource<SetUpAccountResponse>> = _setUpResult

    fun setUp(id: String,setUpAccount: SetUpAccount) {
        viewModelScope.launch {
            _setUpResult.value = Resource.loading(null)
//            try {
//                val response = setUpAccountRepository.setUpAccount(id,setUpAccount,null)
//                if (response.isSuccessful) {
//                    val setUpResponse = response.body()
//                    if (setUpResponse != null) {
//                        _setUpResult.value = Resource.success(setUpResponse)
//                    } else {
//                        _setUpResult.value = Resource.error(null, "Set-up response is null")
//                    }
//                } else {
//                    _setUpResult.value = Resource.error(null, "Set-up failed")
//                }
//            } catch (e: HttpException) {
//                _setUpResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
//            } catch (e: Exception) {
//                _setUpResult.value = Resource.error(null, "Network error: ${e.message}")
//            }
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