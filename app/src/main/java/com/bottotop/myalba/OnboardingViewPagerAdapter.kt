package com.bottotop.myalba

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottotop.asset.AssetFragment
import com.bottotop.community.CommunityFragment
import com.bottotop.home.HomeFragment
import com.bottotop.member.MemberFragment
import com.bottotop.schedule.ScheduleFragment


class OnboardingViewPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position % 5) {
            0 -> MemberFragment()
            1 -> CommunityFragment()
            2 -> HomeFragment()
            3 -> ScheduleFragment()
            4 -> AssetFragment()
            else -> {
                Log.e("TAG", "createFragment: test")
                error("error")
            }
        }
    }

    override fun getItemCount(): Int {
        return 200
    }
}

class FragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val items = arrayOf<Fragment>(
        MemberFragment(),
        CommunityFragment(),
        HomeFragment(),
        ScheduleFragment(),
        AssetFragment()
    )

    override fun getItemCount(): Int = 200

    override fun containsItem(itemId: Long): Boolean {
        return super.containsItem(itemId)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun createFragment(position: Int): Fragment {
        val fragment = items[position % 5]
        return fragment
    }

}

