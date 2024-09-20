package com.fpoly.shoes_app.framework.presentation.ui.shoes.review

import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.databinding.FragmentReviewBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.formatReview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>(
    FragmentReviewBinding::inflate,
    ReviewViewModel::class.java
) {
    @Inject
    lateinit var reviewAdapter: ReviewAdapter

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        binding.rcvReview.adapter = reviewAdapter
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.run {
                    headerLayout.tvTitle.text = state.reviews?.size?.formatReview()
                    reviewAdapter.submitList(state.reviews)
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
    }
}