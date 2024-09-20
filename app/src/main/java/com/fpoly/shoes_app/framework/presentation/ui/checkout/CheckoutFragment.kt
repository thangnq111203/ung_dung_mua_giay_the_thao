package com.fpoly.shoes_app.framework.presentation.ui.checkout

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentCheckoutBinding
import com.fpoly.shoes_app.framework.domain.model.Discount
import com.fpoly.shoes_app.framework.domain.model.PaymentArgs
import com.fpoly.shoes_app.framework.domain.model.Ship
import com.fpoly.shoes_app.framework.domain.model.ShippingCheckoutArgs
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import com.fpoly.shoes_app.utility.formatDiscountTitle
import com.fpoly.shoes_app.utility.formatPriceShoe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>(
    FragmentCheckoutBinding::inflate,
    CheckoutViewModel::class.java
) {
    @Inject
    lateinit var cartCheckoutAdapter: CartCheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.SHIPPING_CHECKOUT_REQUEST_KEY) { _, _ -> }

        setFragmentResultListener(RequestKey.DISCOUNT_CHECKOUT_REQUEST_KEY) { _, _ -> }

        setFragmentResultListener(RequestKey.ADDRESS_CHECKOUT_REQUEST_KEY) { _, _ -> }
    }

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.check_out)
            rcvListCart.adapter = cartCheckoutAdapter
        }

        setFragmentResultListener(RequestKey.SHIPPING_CHECKOUT_REQUEST_KEY) { _, bundle ->
            val ship = bundle.getParcelable<Ship>(ResultKey.SHIPPING_CHECKOUT_RESULT_KEY)
            viewModel.handleShipSelected(ship)
        }

        setFragmentResultListener(RequestKey.DISCOUNT_CHECKOUT_REQUEST_KEY) { _, bundle ->
            val discount = bundle.getParcelable<Discount>(ResultKey.DISCOUNT_CHECKOUT_RESULT_KEY)
            viewModel.handleDiscountSelected(discount)
        }

        setFragmentResultListener(RequestKey.ADDRESS_CHECKOUT_REQUEST_KEY) { _, bundle ->
            val address = bundle.getParcelable<Addresse>(ResultKey.ADDRESS_CHECKOUT_RESULT_KEY)
            viewModel.handleAddressSelected(address)
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                cartCheckoutAdapter.submitList(state.shoes)
                binding.run {
                    tvName.text = state.nameAddress
                    tvAddress.text = state.detailAddress
                    tvPhone.text = state.phoneAddress
                    tvAmountTotal.text = state.totalCart.formatPriceShoe()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.ship }
                .collect { ship ->
                    binding.run {
                        tvShip.text = ship?.title ?: getString(R.string.choose_shipping_title)
                        tvPriceShip.text = ship?.price?.formatPriceShoe()
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.discount }
                .collect { discount ->
                    binding.run {
                        tvDiscount.text = discount?.discount?.formatDiscountTitle()
                            ?: getString(R.string.promo_code_title)
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.isAddressNull }
                .collect {
                    binding.run {
                        tvAddressNull.isVisible = it
                        imgShipping.isEnabled = it.not()
                        tvShip.isEnabled = it.not()
                        imgNextShip.isEnabled = it.not()
                        layoutShipping.isEnabled = it.not()
                    }

                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.shipTotalString }
                .collect {
                    binding.tvShipTotal.text = it
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.tvDiscountTotal.text = it.discountTotalString
                binding.imgClear.isVisible = it.isEnableClear
            }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.totalString }
                .collect {
                    binding.tvTotal.text = it
                }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.isEnableButton }
                .collect { isEnable ->
                    binding.tvButton.isEnabled = isEnable
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

        binding.layoutAddress.setOnClickListener {
            navController?.navigate(
                CheckoutFragmentDirections.actionCheckoutFragmentToAddressCheckoutFragment(
                    viewModel.uiState.value.addressArgs?.id.orEmpty()
                ),
            )
        }

        binding.layoutShipping.setOnClickListener {
            navController?.navigate(
                CheckoutFragmentDirections.actionCheckoutFragmentToShippingCheckoutFragment(
                    ShippingCheckoutArgs(
                        isHaNoi = viewModel.uiState.value.isHaNoi,
                        shipSelected = viewModel.uiState.value.ship?.id,
                    )
                ),
            )
        }

        binding.layoutDiscount.setOnClickListener {
            navController?.navigate(
                CheckoutFragmentDirections.actionCheckoutFragmentToDiscountCheckoutFragment(
                    viewModel.uiState.value.discount?.id.orEmpty(),
                ),
            )
        }

        binding.tvButton.setOnClickListener {
            navController?.navigate(
                CheckoutFragmentDirections.actionCheckoutFragmentToPaymentCheckoutFragment(
                    PaymentArgs(
                        idAddress = viewModel.uiState.value.addressArgs?.id.orEmpty(),
                        shoesCart = viewModel.uiState.value.shoes,
                        totalShip = viewModel.uiState.value.shipTotal,
                        total = viewModel.uiState.value.totalCart,
                    ),
                )
            )
        }

        binding.imgClear.setOnClickListener {
            viewModel.clearDiscount()
        }
    }
}