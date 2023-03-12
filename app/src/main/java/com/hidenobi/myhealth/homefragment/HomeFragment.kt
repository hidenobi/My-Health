package com.hidenobi.myhealth.homefragment

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hidenobi.myhealth.data.stepcounter.StepCounterDatabase
import com.hidenobi.myhealth.data.stepcounter.StepCounterRepository
import com.hidenobi.myhealth.data.waterintake.WaterIntakeDatabase
import com.hidenobi.myhealth.data.waterintake.WaterIntakeRepository
import com.hidenobi.myhealth.databinding.FragmentHomeBinding
import com.hidenobi.myhealth.service.StepCounterService

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var stepCounterIntent: Intent


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = HomeViewModel(
            WaterIntakeRepository(
                WaterIntakeDatabase.getDatabase(requireContext()).waterIntakeDao()
            ),
            StepCounterRepository(
                StepCounterDatabase.getDatabase(requireContext()).stepCounterDao()
            )
        )

        val contextWrapper = ContextWrapper(requireContext())
        stepCounterIntent = Intent(contextWrapper, StepCounterService::class.java)
        contextWrapper.startForegroundService(stepCounterIntent)
        viewModel.bind(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewNumberStep()

    }

    private fun initViewNumberStep() {
        viewModel.searchStepCounterByDate()
        viewModel.searchStepCounter.observe(viewLifecycleOwner) {
            viewModel.setCurrentStepCounter()
            viewModel.currentStepCounter.observe(viewLifecycleOwner) { currentStepCounter ->
                binding.apply {
                    tvNumberOfSteps.text = currentStepCounter.stepCounter.toString()
                    tvNumOfSteps.text = currentStepCounter.stepCounter.toString()
                    tvPercentOfSteps.text =
                        (currentStepCounter.stepCounter / 60).toString().plus("%")
                    tvPercentOfDaily.text =
                        (currentStepCounter.stepCounter / 60 / 3).toString().plus("%")
                    circularProgressBar.progress = (currentStepCounter.stepCounter / 60).toFloat()
                    circularProgressBarDailySleeps.progress =
                        (currentStepCounter.stepCounter / 60).toFloat()
                }
            }
        }
    }

}