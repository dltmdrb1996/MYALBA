package com.bottotop.register.register.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bottotop.core.ext.showToast
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.register.databinding.FragmentManagerBinding
import com.bottotop.register.register.RegisterViewModel
import com.bottotop.register.register.alba.AlbaBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ManagerFragment : Fragment() {

    private val parentViewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentManagerBinding? = null
    private val binding: FragmentManagerBinding get() = _binding!!
    private lateinit var bottomSheet : ManagerBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentManagerBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = ManagerBottomSheet(parentViewModel)
        completeRegister()
        setBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setBtn(){
        binding.apply {
            btnReg.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    fun completeRegister(){
        parentViewModel.managerComplete.observe(viewLifecycleOwner,{
            bottomSheet.dismiss()
            if(it) (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.HomeFlow("home"))
        })
    }

}