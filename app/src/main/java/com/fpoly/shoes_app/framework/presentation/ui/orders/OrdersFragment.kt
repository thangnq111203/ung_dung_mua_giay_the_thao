package com.fpoly.shoes_app.framework.presentation.ui.orders

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOrdersBinding
import com.fpoly.shoes_app.framework.adapter.order.ViewPagerAdapter
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>(
    FragmentOrdersBinding::inflate,
    OrdersViewModel::class.java
) {
    override fun setupPreViews() {

    }

    override fun setupViews() {
        val adapter = ViewPagerAdapter(this)
        (requireActivity() as MainActivity).showBottomNavigation(true)
        binding.apply { viewPager.adapter = adapter
            headerLayout.tvTitle.text = getString(R.string.myOder)
            headerLayout.imgBack.isVisible = false
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Đang hoạt động"
                1 -> "Đã hoàn thành"
                else -> throw IllegalStateException("Unexpected position $position")
            }        }.attach()

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}