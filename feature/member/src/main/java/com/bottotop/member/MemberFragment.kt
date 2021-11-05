package com.bottotop.member

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.base.BaseFragment
import com.bottotop.core.ext.setOnSingleClickListener
import com.bottotop.core.ext.showToast
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.member.databinding.FragmentMemberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberFragment :
    BaseFragment<FragmentMemberBinding, MemberViewModel>(R.layout.fragment_member, "맴버_프래그먼트") {

    private val vm by viewModels<MemberViewModel>()
    override val viewModel get() = vm
    val red = listOf<Int>(R.color.red_bg,R.color.red_light_font,R.color.red_deep_font)
    val blue = listOf<Int>(R.color.blue_bg,R.color.blue_light_font,R.color.blue_deep_font)
    val yelloow = listOf<Int>(R.color.yellow_bg,R.color.yellow_light_font,R.color.yellow_deep_font)
    val green = listOf<Int>(R.color.green_bg,R.color.green_light_font,R.color.green_deep_font)

    private val adapter : MemberAdapter by lazy { MemberAdapter(viewModel) }
    override fun setBindings() {
        _binding?.viewModel = viewModel
        _binding?.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        val list = listOf<MemberModel>(
            MemberModel("이승규","on","26","010-4195-9026","dltmdrb1996@naver.com","10000",blue),
            MemberModel("이승규","on","26","010-4195-9026","dltmdrb1996@naver.com","10000",yelloow),
            MemberModel("이승규","on","26","010-4195-9026","dltmdrb1996@naver.com","10000",red),
            MemberModel("이승규","on","26","010-4195-9026","dltmdrb1996@naver.com","10000",green),
            MemberModel("이승규","on","26","010-4195-9026","dltmdrb1996@naver.com","10000",blue),
        )
        adapter.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            Log.e(TAG, "observeLoading: ${it}",)
            (requireActivity() as ShowLoading).showLoading(it)
        })
    }

}