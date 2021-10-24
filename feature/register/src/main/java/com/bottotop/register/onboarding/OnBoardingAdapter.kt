package com.bottotop.register.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottotop.register.R
import com.bottotop.register.databinding.OnboardViewholderBinding


class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    class ViewHolder constructor(val binding: OnboardViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos : Int) {
            binding.apply {
                when(pos){
                    0 -> {
                        binding.onboardStep.setImageResource(R.drawable.step1)
                        binding.boardImg.setImageResource(R.drawable.onboard_1)
                        binding.onboardTvMain.text = "환영합니다 내알빠는 쉽게 \n직원들을 관리해주는 앱입니다."
                        binding.onboardTvSub.text = "     가게의 스케줄관리 , 커뮤니티와 공지 \n급여관리등 많은 작업들을 보조해줍니다."
                    }
                    1 -> {
                        binding.onboardStep.setImageResource(R.drawable.step2)
                        binding.boardImg.setImageResource(R.drawable.onboard_2)
                        binding.onboardTvMain.text = "편리해지는 가게운영!"
                        binding.onboardTvSub.text = "한눈에 직원들의 일정과 급여를 확인하고 관리\n          커뮤니티를 통한 쉬운 공지와 연락가능"
                    }
                    2 -> {
                        binding.onboardStep.setImageResource(R.drawable.step3)
                        binding.boardImg.setImageResource(R.drawable.onboard_3)
                        binding.onboardTvMain.text = "즐거워지는 알바생활!"
                        binding.onboardTvSub.text = "내알빠는 와이파이와 QR을 통한 쉬운 출석이 가능\n   일한시간과 급여를 확인하며 일하는 즐거움 UP!"
                    }
                    3 -> {
                        binding.onboardStep.setImageResource(R.drawable.step4)
                        binding.boardImg.setImageResource(R.drawable.onboard_4)
                        binding.onboardTvMain.text = "지금 가게를 등록하고 서비스를 이용하세요"
                        binding.onboardTvSub.text = ""
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OnboardViewholderBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

}


