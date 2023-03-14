package com.hidenobi.myhealth.homefragment

import android.Manifest
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hidenobi.myhealth.R
import com.hidenobi.myhealth.data.stepcounter.StepCounterDatabase
import com.hidenobi.myhealth.data.stepcounter.StepCounterRepository
import com.hidenobi.myhealth.data.waterintake.WaterIntakeDatabase
import com.hidenobi.myhealth.data.waterintake.WaterIntakeRepository
import com.hidenobi.myhealth.databinding.FragmentHomeBinding
import com.hidenobi.myhealth.other.ConstantNumber
import com.hidenobi.myhealth.service.StepCounterService

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var stepCounterIntent: Intent

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted:Boolean ->
        if(isGranted){
            // Permission is granted
            val contextWrapper = ContextWrapper(requireContext())
            stepCounterIntent = Intent(contextWrapper, StepCounterService::class.java)
            contextWrapper.startForegroundService(stepCounterIntent)
        }
        else{
            Toast.makeText(requireContext(), R.string.notification_step_counter, Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACTIVITY_RECOGNITION)==PackageManager.PERMISSION_GRANTED){
            val contextWrapper = ContextWrapper(requireContext())
            stepCounterIntent = Intent(contextWrapper, StepCounterService::class.java)
            contextWrapper.startForegroundService(stepCounterIntent)
        }
        else{
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),ConstantNumber.PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION)
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewNumberStep()
        viewModel.bind(binding)
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
                        (currentStepCounter.stepCounter * 100 / ConstantNumber.MAX_STEP).toString()
                            .plus("%")
                    tvPercentOfDaily.text =
                        (currentStepCounter.stepCounter * 100 / ConstantNumber.MAX_STEP / 3).toString()
                            .plus("%")
                    circularProgressBar.progress =
                        (currentStepCounter.stepCounter * 100 / ConstantNumber.MAX_STEP).toFloat()
                    circularProgressBarDailySleeps.progress =
                        (currentStepCounter.stepCounter * 100 / ConstantNumber.MAX_STEP).toFloat()
                }
            }
        }
    }


}