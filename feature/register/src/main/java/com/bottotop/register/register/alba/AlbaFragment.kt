package com.bottotop.register.register.alba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bottotop.register.databinding.FragmentAlbaBinding
import com.bottotop.register.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbaFragment : Fragment() {

    private var _binding: FragmentAlbaBinding? = null
    private val binding: FragmentAlbaBinding get() = _binding!!

    private val parentViewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAlbaBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtn()
        parentViewModel.showToast("알바화면")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setBtn(){
        binding.apply {
            btnCode.setOnClickListener {
                val bottomSheet = AlbaBottomSheet(parentViewModel)
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

}