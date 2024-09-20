package com.fpoly.shoes_app.framework.presentation.ui.checkout.shipping

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentShippingCheckoutBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShippingCheckoutFragment :
    BaseFragment<FragmentShippingCheckoutBinding, ShippingCheckoutViewModel>(
        FragmentShippingCheckoutBinding::inflate,
        ShippingCheckoutViewModel::class.java
    ) {
    @Inject
    lateinit var shippingCheckoutAdapter: ShippingCheckoutAdapter

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.choose_shipping_title)
            rcvShipping.adapter = shippingCheckoutAdapter
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState
                .map { it.shipsPair }
                .collect { shipPair ->
                    shippingCheckoutAdapter.submitList(shipPair)
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.isLoading
            }.distinctUntilChanged()
                .collect {
                    showProgressbar(it)
                }
        }
    }

    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        shippingCheckoutAdapter.setOnClick {
            val result = Bundle().apply {
                putParcelable(ResultKey.SHIPPING_CHECKOUT_RESULT_KEY, it)
            }
            setFragmentResult(RequestKey.SHIPPING_CHECKOUT_REQUEST_KEY, result)
            navController?.popBackStack()
        }
    }
}