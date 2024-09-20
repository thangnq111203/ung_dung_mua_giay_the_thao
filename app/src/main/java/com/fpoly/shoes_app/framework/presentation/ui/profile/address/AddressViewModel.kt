package com.fpoly.shoes_app.framework.presentation.ui.profile.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.repository.AllAddressRepository
import com.fpoly.shoes_app.framework.data.repository.DeleteAddressRepository
import com.fpoly.shoes_app.framework.domain.model.profile.address.AllAddressResponse
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val allAddressRepository: AllAddressRepository,
    private val deleteAddressRepository: DeleteAddressRepository
) : ViewModel() {
    private val _allAddressResult = MutableStateFlow<Resource<AllAddressResponse>>(Resource.init(null))
    val allAddressResult: StateFlow<Resource<AllAddressResponse>> = _allAddressResult

    private val _deleteAddressResult = MutableStateFlow<Resource<AllAddressResponse>>(Resource.init(null))
    val deleteAddressResult: StateFlow<Resource<AllAddressResponse>> = _deleteAddressResult

    fun fetchAllAddresses(id: String) {
        viewModelScope.launch {
            _allAddressResult.value = Resource.loading(null)
            try {
                val response = allAddressRepository.allAddress(id)
                if (response.isSuccessful) {
                    val allAddressResponse = response.body()
                    if (allAddressResponse != null) {
                        _allAddressResult.value = Resource.success(allAddressResponse)
                    } else {
                        _allAddressResult.value = Resource.error(null, "Response body is null")
                    }
                } else {
                    _allAddressResult.value = Resource.success( null)
                }
            } catch (e: HttpException) {
                _allAddressResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _allAddressResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            _deleteAddressResult.value = Resource.loading(null)
            try {
                val response = deleteAddressRepository.deleteAddress(id)
                if (response.isSuccessful) {
                    val deleteAddressResponse = response.body()
                    if (deleteAddressResponse != null) {
                        _deleteAddressResult.value = Resource.success(deleteAddressResponse)
                    } else {
                        _deleteAddressResult.value = Resource.error(null, "Response body is null")
                    }
                } else {
                    _deleteAddressResult.value = Resource.error(null, "Failed to delete address")
                }
            } catch (e: HttpException) {
                _deleteAddressResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _deleteAddressResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}
