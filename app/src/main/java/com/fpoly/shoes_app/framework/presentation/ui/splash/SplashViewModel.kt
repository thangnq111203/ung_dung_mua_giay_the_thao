package com.fpoly.shoes_app.framework.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.PageSplash
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> get() = _uiState

    private val _singleEvent = MutableStateFlow<SplashSingleEvent?>(null)
    val singleEvent: Flow<SplashSingleEvent> get() = _singleEvent.filterNotNull()

    init {
        getData()
        checkSplashScreenStatus()
        Log.d("123123", "123${sharedPreferences.getIdUser()}")
    }

    private fun getData() {
        val list = arrayListOf(
            PageSplash(
                "1",
                "Chào Mừng Đến Với \nShoeBee",
                "Khám phá bộ sưu tập giày thể thao mới nhất và phong cách độc đáo, đem lại cho bạn sự thoải mái và tự tin.",
                R.drawable.img_1
            ),
            PageSplash(
                "2",
                "Thời Trang, \nSang Trọng",
                "Tìm kiếm giày thời trang hoàn hảo cho mọi dịp, từ đi làm đến đi chơi, với nhiều mẫu mã và màu sắc phong phú.",
                R.drawable.img_2
            ),
            PageSplash(
                "3",
                "Nhận Ngay\nƯu Đãi Đặc Biệt",
                "Nhận ngay ưu đãi độc quyền khi mua sắm giày tại đây và trải nghiệm dịch vụ khách hàng tận tình nhất.",
                R.drawable.img_3
            ),
        )
        _uiState.update { state ->
            state.copy(pagesSplash = list)
        }
    }

    private fun checkSplashScreenStatus() {
        if (sharedPreferences.isSplashScreenSkipped())
            navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        if (sharedPreferences.getIdUser().isBlank()) {
            _singleEvent.value = SplashSingleEvent.GoToSignIn
        } else {
            _singleEvent.value = SplashSingleEvent.GoToHome
        }
    }

    fun getPage(currentPage: Int, totalPages: Int) {
        val textButton = if (currentPage >= totalPages - 1) GET_STARED else NEXT
        _uiState.update { state ->
            state.copy(
                page = currentPage,
                textButton = textButton
            )
        }
    }

    fun nextPage(currentPage: Int, totalPages: Int) {
        val isLastPage = currentPage >= totalPages - 1
        if (isLastPage) {
            navigateToNextScreen()
            sharedPreferences.setSplashScreenSkipped(true)
        } else {
            _uiState.update { state ->
                state.copy(
                    page = currentPage + 1
                )
            }
        }
    }

    private companion object {
        const val NEXT = "Tiếp tục"
        const val GET_STARED = "Bắt đầu"
    }
}