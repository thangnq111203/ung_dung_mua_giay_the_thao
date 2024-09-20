package com.fpoly.shoes_app.framework.presentation.ui.favorites

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentFavoritesBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.categories.CategoriesSelectedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    FragmentFavoritesBinding::inflate,
    FavoritesViewModel::class.java,
) {
    @Inject
    lateinit var categoriesSelectedAdapter: CategoriesSelectedAdapter

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.run {
            headerLayout.imgBack.isVisible = false
            headerLayout.tvTitle.text = getString(R.string.favorites_title)
            rcvCategoriesSelected.adapter = categoriesSelectedAdapter
            rcvFavorite.adapter = favoriteAdapter
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                categoriesSelectedAdapter.submitList(state.categoriesSelected)
                favoriteAdapter.submitList(state.favoriteShoes)
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
        categoriesSelectedAdapter.setOnClick {
            viewModel.handleClickCategoriesSelected(it)
            viewModel.getShoesFavorite(it.name)
        }

        favoriteAdapter.setOnClick {
            navController?.navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToShoeDetailFragment(
                    it.id.orEmpty(),
                    false,
                )
            )
        }

        favoriteAdapter.setOnClickFavorite {
            viewModel.deleteFavorite(it.id.orEmpty())
        }
    }
}