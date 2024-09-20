package com.fpoly.shoes_app.framework.presentation.ui.checkout.payment

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.example.zalopaykotlin.Api.CreateOrder
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentPaymentCheckoutBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.json.JSONObject
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

@AndroidEntryPoint
class PaymentCheckoutFragment :
    BaseFragment<FragmentPaymentCheckoutBinding, PaymentCheckoutViewModel>(
        FragmentPaymentCheckoutBinding::inflate,
        PaymentCheckoutViewModel::class.java
    ) {
    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.choose_payment_title)
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.isLoading
            }.distinctUntilChanged()
                .collect {
                    showProgressbar(it)
                }
        }

        lifecycleScope.launch {
            viewModel.singleEvent.collect { event ->
                when (event) {
                    PaymentCheckoutSingleEvent.CheckOut -> {
                        val result = Bundle().apply {
                            putBoolean(ResultKey.SHOW_ALERT_DIALOG_CHECKOUT_RESULT_KEY, true)
                        }
                        setFragmentResult(RequestKey.SHOW_ALERT_DIALOG_CHECKOUT_REQUEST_KEY, result)
                        navController?.popBackStack(R.id.cartFragment, false)
                    }
                }

            }
        }
    }

    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        binding.layoutCOD.setOnClickListener {
            viewModel.handleCheckout(getString(R.string.cod_text))
        }

        binding.layoutZaloPay.setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {
        lifecycleScope.launch {
            val orderApi = CreateOrder()

            val data: JSONObject? = try {
                orderApi.createOrder(viewModel.uiState.value.args?.total.toString())
            } catch (e: Exception) {
                null
            }

            val returnCode = data?.optString(RETURN_CODE)

            if (returnCode == RETURN_CODE_CHECK) {
                val token = data.getString(ZALO_TOKEN)
                ZaloPaySDK.getInstance().payOrder(
                    requireActivity(),
                    token,
                    ZALO_DK,
                    object : PayOrderListener {
                        override fun onPaymentSucceeded(
                            transactionId: String,
                            transToken: String,
                            appTransID: String
                        ) {
                            viewModel.handleCheckout(getString(R.string.zalo_pay_text))
                        }

                        override fun onPaymentCanceled(
                            zpTransToken: String,
                            appTransID: String
                        ) {
                        }

                        override fun onPaymentError(
                            zaloPayError: ZaloPayError,
                            zpTransToken: String,
                            appTransID: String
                        ) {
                            showAlertDialog(
                                title = getString(R.string.checkout_error),
                                button = getString(R.string.checkout_error_button),
                                onClick = { }
                            )
                        }
                    }
                )

            }
        }
    }

    private companion object {
        private const val RETURN_CODE = "returncode"
        private const val RETURN_CODE_CHECK = "1"
        private const val ZALO_TOKEN = "zptranstoken"
        private const val ZALO_DK = "demozpdk://app"
    }
}