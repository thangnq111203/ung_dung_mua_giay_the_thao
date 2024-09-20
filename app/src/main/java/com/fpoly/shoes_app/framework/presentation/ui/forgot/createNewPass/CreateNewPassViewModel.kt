package com.fpoly.shoes_app.framework.presentation.ui.forgot.createNewPass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPassResponse
import com.fpoly.shoes_app.framework.data.repository.CreateNewPassRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CreateNewPassViewModel@Inject constructor(

    private val createNewPassRepository: CreateNewPassRepository
) : ViewModel() {
    private val _createPassResult = MutableStateFlow<Resource<NewPassResponse>>(Resource.init(null))
    val createPassResult: StateFlow<Resource<NewPassResponse>> = _createPassResult

    fun newPass(newPass: NewPass) {
        viewModelScope.launch {
            _createPassResult.value = Resource.loading(null)
            try {
                val response = createNewPassRepository.newPass(newPass)
                if (response.isSuccessful) {
                    val createPassResponse = response.body()
                    if (createPassResponse != null) {
                        _createPassResult.value = Resource.success(createPassResponse)
                    } else {
                        _createPassResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _createPassResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _createPassResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _createPassResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}