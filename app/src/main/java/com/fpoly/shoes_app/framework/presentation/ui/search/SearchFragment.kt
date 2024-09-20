package com.fpoly.shoes_app.framework.presentation.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSearchBinding
import com.fpoly.shoes_app.framework.domain.model.FilterArgs
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.shoes.ShoesAdapter
import com.fpoly.shoes_app.utility.RatingText
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import com.fpoly.shoes_app.utility.SortText
import com.fpoly.shoes_app.utility.formatSizeSearch
import com.fpoly.shoes_app.utility.formatTextSearch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    FragmentSearchBinding::inflate,
    SearchViewModel::class.java
) {
    @Inject
    lateinit var shoesAdapter: ShoesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.KEY_FILTER_REQUEST_KEY) { _, _ -> }
    }

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.search)
            rcvShoes.adapter = shoesAdapter
        }

        setFragmentResultListener(RequestKey.KEY_FILTER_REQUEST_KEY) { _, bundle ->
            val filter = bundle.getParcelable<FilterArgs>(ResultKey.KEY_FILTER_RESULT_KEY)

            viewModel.getDataShoes(
                name = viewModel.uiState.value.textSearch.orEmpty(),
                category = filter?.idCategory,
                sort = filter?.idSort ?: SortText.MOST_RECENT,
                rating = filter?.rating ?: RatingText.RATING_ALL,
                priceMin = filter?.startPrice ?: 0L,
                priceMax = filter?.endPrice ?: 0L,
            )
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                shoesAdapter.submitList(state.shoes)
                binding.run {
                    tvSearch.text = state.textSearch?.formatTextSearch()
                    tvItemSearch.text = state.shoes.size.formatSizeSearch()
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
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        shoesAdapter.setOnClick {
            navController?.navigate(
                SearchFragmentDirections.actionSearchFragmentToShoeDetailFragment(
                    it.id.orEmpty(),
                    false,
                )
            )
        }
        shoesAdapter.setOnClickFavorite {
            if (it.second) viewModel.deleteFavorite(it.first.id.orEmpty())
            else viewModel.addFavorite(it.first.id.orEmpty())
        }

        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.getDataShoes(
                    binding.edtSearch.text.toString(),
                    viewModel.uiState.value.category,
                    viewModel.uiState.value.sort,
                    viewModel.uiState.value.rating,
                    viewModel.uiState.value.priceMin,
                    viewModel.uiState.value.priceMax,
                )
                (requireActivity() as? MainActivity)?.hideKeyboard()
                binding.tvSearch.clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.imgFilter.setOnClickListener {
            navController?.navigate(
                SearchFragmentDirections.actionSearchFragmentToFilterFragment(
                    FilterArgs(
                        idCategory = viewModel.uiState.value.category,
                        startPrice = viewModel.uiState.value.priceMin,
                        endPrice = viewModel.uiState.value.priceMax,
                        idSort = viewModel.uiState.value.sort,
                        rating = viewModel.uiState.value.rating,
                    )
                )
            )
        }
    }
}