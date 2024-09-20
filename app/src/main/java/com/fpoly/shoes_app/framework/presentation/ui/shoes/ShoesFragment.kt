package com.fpoly.shoes_app.framework.presentation.ui.shoes

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.fpoly.shoes_app.databinding.FragmentShoesBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.categories.CategoriesSelectedAdapter
import com.fpoly.shoes_app.utility.GET_ALL_POPULAR_SHOES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShoesFragment : BaseFragment<FragmentShoesBinding, ShoesViewModel>(
    FragmentShoesBinding::inflate,
    ShoesViewModel::class.java
) {
    private val args: ShoesFragmentArgs by navArgs()

    @Inject
    lateinit var categoriesSelectedAdapter: CategoriesSelectedAdapter

    @Inject
    lateinit var shoesAdapter: ShoesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDataShoes(type = args.titleShoes)
        navController?.popBackStack()
    }

    override fun setupViews() {
        binding.headerLayout.run {
            tvTitle.text = args.titleShoes
            imgSearch.isVisible = true
        }
        setupRecyclerView()
    }

    override fun bindViewModel() {
        initHandleUiState()
    }

    override fun setOnClick() {
        binding.run {
            headerLayout.imgBack.setOnClickListener {
                navController?.popBackStack()
            }

            rcvCategoriesSelected.isVisible = args.titleShoes == GET_ALL_POPULAR_SHOES
        }
        categoriesSelectedAdapter.setOnClick {
            viewModel.handleClickCategoriesSelected(it)
            viewModel.getDataShoes(it.name, args.titleShoes)
        }

        shoesAdapter.setOnClick {
            navController?.navigate(
                ShoesFragmentDirections.actionShoesFragmentToShoeDetailFragment(
                    it.id.orEmpty(),
                    false,
                )
            )
        }

        shoesAdapter.setOnClickFavorite {
            if (it.second) viewModel.deleteFavorite(it.first.id.orEmpty())
            else viewModel.addFavorite(it.first.id.orEmpty())
        }

        binding.headerLayout.imgSearch.setOnClickListener {
            navController?.navigate(ShoesFragmentDirections.actionShoesFragmentToSearchFragment())
        }
    }

    private fun initHandleUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                categoriesSelectedAdapter.submitList(state.categoriesSelected)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.shoes
            }.distinctUntilChanged().collect {
                shoesAdapter.submitList(it)
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

    private fun setupRecyclerView() {
        binding.apply {
            rcvCategoriesSelected.adapter = categoriesSelectedAdapter
            rcvShoes.adapter = shoesAdapter
        }
    }
}