package com.fpoly.shoes_app.framework.presentation.ui.checkout.discount

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentDiscountCheckoutBinding
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
class DiscountCheckoutFragment :
    BaseFragment<FragmentDiscountCheckoutBinding, DiscountCheckoutViewModel>(
        FragmentDiscountCheckoutBinding::inflate,
        DiscountCheckoutViewModel::class.java
    ) {
    @Inject
    lateinit var discountCheckoutAdapter: DiscountCheckoutAdapter

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.promo_code_title)
            rcvDiscount.adapter = discountCheckoutAdapter
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState
                .map { it.discountsPair }
                .collect { discountsPair ->
                    discountCheckoutAdapter.submitList(discountsPair)
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.isVisibleTextEmpty
            }.distinctUntilChanged().collect {
                binding.tvListNull.isVisible = it
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

        discountCheckoutAdapter.setOnClick {
            val result = Bundle().apply {
                putParcelable(ResultKey.DISCOUNT_CHECKOUT_RESULT_KEY, it)
            }
            setFragmentResult(RequestKey.DISCOUNT_CHECKOUT_REQUEST_KEY, result)
            navController?.popBackStack()
        }
    }
}