package com.fpoly.shoes_app.framework.presentation.ui.cart

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentCartBinding
import com.fpoly.shoes_app.framework.domain.model.CheckoutArgs
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.PLUS
import com.fpoly.shoes_app.utility.REDUCE
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import com.fpoly.shoes_app.utility.formatPriceShoe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(
    FragmentCartBinding::inflate,
    CartViewModel::class.java
) {

    @Inject
    lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.RELOAD_CART_REQUEST_KEY) { _, _ -> }
    }

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.imgBack.isVisible = false
            headerLayout.tvTitle.text = getString(R.string.cart_title)
            rcvCart.adapter = cartAdapter
        }

        setFragmentResultListener(RequestKey.RELOAD_CART_REQUEST_KEY) { _, bundle ->
            val isReload = bundle.getBoolean(ResultKey.RELOAD_CART_RESULT_KEY)
            if (isReload) {
                viewModel.getDataCart()
            }
        }

        setFragmentResultListener(RequestKey.SHOW_ALERT_DIALOG_CHECKOUT_REQUEST_KEY) { _, bundle ->
            val isEnable = bundle.getBoolean(ResultKey.SHOW_ALERT_DIALOG_CHECKOUT_RESULT_KEY)
            if (isEnable) {
                viewModel.getDataCart()
                showAlertDialog(
                    imgSuccess = true,
                    title = getString(R.string.checkout_success),
                    button = getString(R.string.checkout_success_button),
                    onClick = { }
                )
            }
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                cartAdapter.submitList(state.shoes)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .mapNotNull { it.isEnableButton }
                .collect { enable ->
                    binding.tvButton.isEnabled = enable
                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .mapNotNull { it.total }
                .collect { total ->
                    binding.tvPriceTotal.text = total.formatPriceShoe()
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
            }.distinctUntilChanged().collect {
                showProgressbar(it)
            }
        }
    }

    override fun setOnClick() {
        cartAdapter.setOnClick {
            navController?.navigate(
                CartFragmentDirections.actionCartFragmentToShoeDetailFragment(it, true)
            )
        }

        cartAdapter.setOnClickRemove {
            showAlertDialog(
                title = getString(R.string.cart_delete_title),
                button = getString(R.string.agree),
                buttonCancel = getString(R.string.cancel),
                onClick = { viewModel.removeShoeCart(it) }
            )
        }

        cartAdapter.setOnClickPlus {
            viewModel.updateShoe(it, PLUS)
        }

        cartAdapter.setOnClickReduce {
            viewModel.updateShoe(it, REDUCE)
        }

        binding.tvButton.setOnClickListener {
            navController?.navigate(
                CartFragmentDirections.actionCartFragmentToCheckoutFragment(
                    CheckoutArgs(
                        carts = viewModel.uiState.value.carts,
                        totalCart = viewModel.uiState.value.total ?: 0,
                    )
                )
            )
        }
    }
}