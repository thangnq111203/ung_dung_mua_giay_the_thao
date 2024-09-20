package com.fpoly.shoes_app.framework.presentation.ui.banner

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.fpoly.shoes_app.databinding.FragmentBannerDetailBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BannerDetailFragment : BaseFragment<FragmentBannerDetailBinding, BannerDetailViewModel>(
    FragmentBannerDetailBinding::inflate,
    BannerDetailViewModel::class.java
) {
    private val args: BannerDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBannerDetail(args.banner.id.orEmpty())
    }

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = TITLE_BANNER
        }
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.run {
                    tvTitleBanner.text = state.title
                    imgBanner.loadImage(state.image)
                    tvDescription.text = state.description
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

    private companion object {
        private const val TITLE_BANNER = "Tin mới và ưu đãi"
    }
}