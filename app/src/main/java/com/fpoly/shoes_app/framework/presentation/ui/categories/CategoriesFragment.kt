package com.fpoly.shoes_app.framework.presentation.ui.categories

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fpoly.shoes_app.databinding.FragmentCategoriesBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.SPAN_COUNT_CATEGORIES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>(
    FragmentCategoriesBinding::inflate,
    CategoriesViewModel::class.java
) {

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    override fun setupViews() {
        setupRecyclerView()
        binding.headerLayout.run {
            tvTitle.text = TITLE_CATEGORY
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                categoriesAdapter.submitList(state.categories)
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
        categoriesAdapter.setOnClick {
            navController?.navigate(
                CategoriesFragmentDirections.actionCategoriesFragmentToShoesFragment(it.name ?: "")
            )
        }

        binding.run {
            headerLayout.imgBack.setOnClickListener {
                navController?.popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            rcvCategory.run {
                layoutManager = StaggeredGridLayoutManager(
                    SPAN_COUNT_CATEGORIES,
                    StaggeredGridLayoutManager.VERTICAL
                )
                adapter = categoriesAdapter
            }
        }
    }

    private companion object {
        private const val TITLE_CATEGORY = "Thể loại"
    }
}