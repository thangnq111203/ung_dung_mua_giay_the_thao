package com.fpoly.shoes_app.framework.presentation.ui.orders.history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentHistoryBinding
import com.fpoly.shoes_app.framework.adapter.order.HistoryAdapter
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.orders.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, OrdersViewModel>(
    FragmentHistoryBinding::inflate, OrdersViewModel::class.java
) {
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var idUser: String
    override fun setupPreViews() {
    }
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)
        idUser = sharedPreferences.getIdUser()
        historyAdapter = HistoryAdapter(
            historyShoes = emptyList(),
            onClickActive = null,
            onClickComplete = { history ->
                val bundle = Bundle().apply {
                    putParcelable("history", history)
                }
                findNavController().navigate(R.id.detailHistoryFragment, bundle)
            })
        binding.apply {
            recycViewHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = historyAdapter
            }
            swipeRefreshLayout.setOnRefreshListener {
                clearListAndFetchData()
            }
        }
        clearListAndFetchData()
    }
    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = state.isLoading
                if (!state.isLoading) {
                    historyAdapter.updateHistoryShoes(state.historyShoes)
                    state.errorMessage?.let {
                        binding.textNoData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun clearListAndFetchData() {
        viewModel.getCompletedOrders(idUser)
    }
    override fun setOnClick() {
    }
}