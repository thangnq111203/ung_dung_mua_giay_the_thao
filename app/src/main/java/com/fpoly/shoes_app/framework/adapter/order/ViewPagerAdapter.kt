package com.fpoly.shoes_app.framework.adapter.order

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fpoly.shoes_app.framework.presentation.ui.orders.history.HistoryFragment
import com.fpoly.shoes_app.framework.presentation.ui.orders.active.ActiveFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveFragment()
            1 -> HistoryFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}