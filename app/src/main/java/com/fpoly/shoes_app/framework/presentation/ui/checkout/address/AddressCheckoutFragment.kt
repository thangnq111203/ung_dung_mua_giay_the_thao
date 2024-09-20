package com.fpoly.shoes_app.framework.presentation.ui.checkout.address

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressCheckoutBinding
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
class AddressCheckoutFragment :
    BaseFragment<FragmentAddressCheckoutBinding, AddressCheckoutViewModel>(
        FragmentAddressCheckoutBinding::inflate,
        AddressCheckoutViewModel::class.java
    ) {
    @Inject
    lateinit var addressCheckoutAdapter: AddressCheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.RELOAD_ADDRESS_CHECKOUT_REQUEST_KEY) { _, _ -> }
    }

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.choose_address_title)
            rcvAddress.adapter = addressCheckoutAdapter
        }

        setFragmentResultListener(RequestKey.RELOAD_ADDRESS_CHECKOUT_REQUEST_KEY) { _, bundle ->
            val check = bundle.getBoolean(ResultKey.RELOAD_ADDRESS_CHECKOUT_RESULT_KEY)
            if (check) viewModel.getAddress()
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState
                .map { it.addressPair }
                .collect { addressPair ->
                    addressCheckoutAdapter.submitList(addressPair)
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

        addressCheckoutAdapter.setOnClick {
            val result = Bundle().apply {
                putParcelable(ResultKey.ADDRESS_CHECKOUT_RESULT_KEY, it)
            }
            setFragmentResult(RequestKey.ADDRESS_CHECKOUT_REQUEST_KEY, result)
            navController?.popBackStack()
        }

        binding.tvButton.setOnClickListener {
            navController?.navigate(
                AddressCheckoutFragmentDirections
                    .actionAddressCheckoutFragmentToAddressFragment(true),
            )
        }
    }
}