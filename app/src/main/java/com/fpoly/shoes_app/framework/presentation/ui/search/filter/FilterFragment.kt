package com.fpoly.shoes_app.framework.presentation.ui.search.filter

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentFilterBinding
import com.fpoly.shoes_app.framework.domain.model.FilterArgs
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.categories.CategoriesSelectedAdapter
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    FragmentFilterBinding::inflate,
    FilterViewModel::class.java
) {
    @Inject
    lateinit var categoriesSelectedAdapter: CategoriesSelectedAdapter

    @Inject
    lateinit var sortAdapter: SortAdapter

    @Inject
    lateinit var ratingAdapter: RatingAdapter

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.filter)
            rcvCategory.adapter = categoriesSelectedAdapter
            rcvSortBy.adapter = sortAdapter
            rcvRating.adapter = ratingAdapter
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                categoriesSelectedAdapter.submitList(state.categories)
                sortAdapter.submitList(state.sorts)
                ratingAdapter.submitList(state.ratings)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.run {
                    edtMin.setText(state.priceMin.toString())
                    edtMax.setText(state.priceMax.toString())
                }
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
        categoriesSelectedAdapter.setOnClick {
            viewModel.handleClickCategoriesSelected(it.id)
        }

        sortAdapter.setOnClick {
            viewModel.handleClickSortSelected(it.id)
        }

        ratingAdapter.setOnClick {
            viewModel.handleClickRatingSelected(it)
        }

        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        binding.tvApply.setOnClickListener {
            val result = Bundle().apply {
                putParcelable(
                    ResultKey.KEY_FILTER_RESULT_KEY, FilterArgs(
                        idCategory = viewModel.uiState.value.categorySelected,
                        startPrice = viewModel.uiState.value.priceMin,
                        endPrice = viewModel.uiState.value.priceMax,
                        idSort = viewModel.uiState.value.sortSelected,
                        rating = viewModel.uiState.value.ratingSelected,
                    )
                )
            }
            setFragmentResult(RequestKey.KEY_FILTER_REQUEST_KEY, result)
            navController?.popBackStack()
        }

        binding.tvReset.setOnClickListener {
            viewModel.resetUiState()
        }

        binding.edtMin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.handlePriceMin(binding.edtMin.text.toString())
                (requireActivity() as? MainActivity)?.hideKeyboard()
                binding.edtMin.clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.edtMax.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.handlePriceMax(binding.edtMax.text.toString())
                (requireActivity() as? MainActivity)?.hideKeyboard()
                binding.edtMax.clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }
    }
}