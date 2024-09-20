package com.fpoly.shoes_app.framework.presentation.ui.orders.history.detaiHistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRate
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRateResponse
import com.fpoly.shoes_app.framework.repository.RateRepository
import com.fpoly.shoes_app.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DetailHistoryViewModel@Inject constructor(
    private val rateRepository: RateRepository,
) : ViewModel() {
    private val _setUpResult = MutableStateFlow<Resource<UpdateRateResponse>>(Resource.init(null))
    val rateResult: StateFlow<Resource<UpdateRateResponse>> = _setUpResult

    fun rate(rate:UpdateRate) {
        Log.e("rate",rate.shoeId.toString())
        viewModelScope.launch {
            _setUpResult.value = Resource.loading(null)
            try {
                val response = rateRepository.rateUpdate(rate)
                if (response.isSuccessful) {
                    val setUpResponse = response.body()
                    if (setUpResponse != null) {
                        _setUpResult.value = Resource.success(setUpResponse)
                    } else {
                        _setUpResult.value = Resource.error(null, "Set-up response is null")
                    }
                } else {
                    _setUpResult.value = Resource.error(null, "Set-up failed")
                }
            } catch (e: HttpException) {
                _setUpResult.value = Resource.error(null, "HTTP Error: ${e.message()}")
            } catch (e: Exception) {
                _setUpResult.value = Resource.error(null, "Network error: ${e.message}")
            }
        }
    }
}