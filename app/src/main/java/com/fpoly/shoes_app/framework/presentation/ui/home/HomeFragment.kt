package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentHomeBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.banner.BannerAdapter
import com.fpoly.shoes_app.framework.presentation.ui.categories.CategoriesAdapter
import com.fpoly.shoes_app.framework.presentation.ui.categories.CategoriesSelectedAdapter
import com.fpoly.shoes_app.framework.presentation.ui.shoes.ShoesAdapter
import com.fpoly.shoes_app.utility.GET_ALL_POPULAR_SHOES
import com.fpoly.shoes_app.utility.ITEM_MORE
import com.fpoly.shoes_app.utility.SPAN_COUNT_CATEGORIES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {
    private val navOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()

    @Inject
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var categoriesSelectedAdapter: CategoriesSelectedAdapter

    @Inject
    lateinit var shoesAdapter: ShoesAdapter

    override fun setupViews() {
        setupRecyclerView()
    }

    override fun bindViewModel() {
        initHandleUiState()
    }

    override fun setOnClick() {
        binding.notificationHome.setOnClickListener {
            val fragmentId = R.id.notificationHomeFragment
            val navController = findNavController()
            val currentDestination = navController.currentDestination
            if (currentDestination == null || currentDestination.id != fragmentId) {
                navController.navigate(fragmentId, null, navOptions)
            }
        }

        setOnClickCategorySelected()
        binding.run {
            tvAllPopularShoes.setOnClickListener {
                navController?.navigate(
                    HomeFragmentDirections.actionHomeFragmentToShoesFragment(GET_ALL_POPULAR_SHOES)
                )
            }
        }

        categoriesAdapter.setOnClick {
            if (it.name == ITEM_MORE) {
                navController?.navigate(R.id.action_homeFragment_to_categoriesFragment)
            } else {
                navController?.navigate(
                    HomeFragmentDirections.actionHomeFragmentToShoesFragment(it.name ?: "")
                )
            }
        }

        shoesAdapter.setOnClick {
            navController?.navigate(
                HomeFragmentDirections.actionHomeFragmentToShoeDetailFragment(
                    it.id.orEmpty(),
                    false,
                )
            )
        }

        bannerAdapter.setOnClick {
            navController?.navigate(
                HomeFragmentDirections.actionHomeFragmentToBannerDetailFragment(it)
            )
        }

        shoesAdapter.setOnClickFavorite {
            if (it.second) viewModel.deleteFavorite(it.first.id.orEmpty())
            else viewModel.addFavorite(it.first.id.orEmpty())
        }

        binding.layoutSearch.setOnClickListener {
            navController?.navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            )
        }
    }

    private fun initHandleUiState() {
        lifecycleScope.launch {
            viewModel.uiState
                .mapNotNull { it.banners }
                .collect {
                    bannerAdapter.submitList(it)
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                categoriesAdapter.submitList(state.categories)
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
                it.isLoading
            }.distinctUntilChanged().collect {
                showProgressbar(it)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.run {
                    tvName.text = state.nameUser
                    Glide.with(requireContext())
                        .load(state.imageUser)
                        .error(R.drawable.ic_user_image)
                        .into(imgAvatar)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            viewPagerBanner.adapter = bannerAdapter
            binding.dotsIndicator.attachTo(viewPagerBanner)
            rcvCategory.run {
                layoutManager = StaggeredGridLayoutManager(
                    SPAN_COUNT_CATEGORIES,
                    StaggeredGridLayoutManager.VERTICAL
                )
                adapter = categoriesAdapter
            }
            rcvCategoriesSelected.adapter = categoriesSelectedAdapter
            rcvShoes.adapter = shoesAdapter
        }
    }

    private fun setOnClickCategorySelected() {
        categoriesSelectedAdapter.setOnClick {
            viewModel.handleClickCategoriesSelected(it)
            viewModel.getDataPopularShoes(it.name)
        }
    }
}