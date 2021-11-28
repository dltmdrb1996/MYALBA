package com.bottotop.register.register.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.ToFlowNavigation
import com.bottotop.register.databinding.FragmentManagerBinding
import com.bottotop.register.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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
        initClickEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickEvent(){
        binding.apply {
            btnReg.setOnClickListener {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun completeRegister(){
        parentViewModel.managerComplete.observe(viewLifecycleOwner,{
            Timber.e("completeRegister: $it")
            if(it) (requireActivity() as ToFlowNavigation).navigateToFlow(NavigationFlow.HomeFlow("home"))
        })
    }

}