package com.fpoly.shoes_app.framework.presentation.ui.profile.editOrAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.AddAddressRepository
import com.fpoly.shoes_app.framework.data.repository.UpdateAddressRepository
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress

import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditOrAddViewModel  @Inject constructor(
    private val addAddressRepository: AddAddressRepository,
    private val updateAddressRepository: UpdateAddressRepository
) : ViewModel() {
    private val _addAddressResult = MutableStateFlow<Resource<AllAddressResponse>>(Resource.init(null))
    val addAddressResult: StateFlow<Resource<AllAddressResponse>> = _addAddressResult
    private val _updateAddressResult = MutableStateFlow<Resource<AllAddressResponse>>(Resource.init(null))
    val updateAddressResult: StateFlow<Resource<AllAddressResponse>> = _updateAddressResult

    fun addAddress(address: AddAddress) {
        viewModelScope.launch {
            _addAddressResult.value = Resource.loading(null)

            try {
                val response = addAddressRepository.addAddress(address)
                if (response.isSuccessful) {
                    val setUpResponse = response.body()
                    if (setUpResponse != null) {
                        _addAddressResult.value = Resource.success(setUpResponse)

                    } else {
                        _addAddressResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _addAddressResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _addAddressResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _addAddressResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
    fun updateAddress(id: String,address: AddAddress) {
        viewModelScope.launch {
            _updateAddressResult.value = Resource.loading(null)
            try {
                val response = updateAddressRepository.updateAddress(id,address)
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    if (profileResponse != null) {
                        _updateAddressResult.value = Resource.success(profileResponse)
                    } else {
                        _updateAddressResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _updateAddressResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _updateAddressResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _updateAddressResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}