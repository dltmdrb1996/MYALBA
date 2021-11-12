package com.bottotop.register.register.alba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigation
import com.bottotop.register.databinding.FragmentAlbaBinding
import com.bottotop.register.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbaFragment : Fragment() {

    private val parentViewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentAlbaBinding? = null
    private val binding: FragmentAlbaBinding get() = _binding!!
    private lateinit var bottomSheet : AlbaBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAlbaBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet=AlbaBottomSheet(parentViewModel)
        completeRegister()
        initClickEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickEvent(){
        binding.apply {
            btnCode.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun completeRegister(){
        parentViewModel.albaComplete.observe(viewLifecycleOwner,{
            bottomSheet.dismiss()
            if(it) (requireActivity() as ToFlowNavigation).navigateToFlow(NavigationFlow.HomeFlow("home"))
        })
    }

}