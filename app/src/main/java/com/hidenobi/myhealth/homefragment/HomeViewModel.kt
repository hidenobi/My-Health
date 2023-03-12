package com.hidenobi.myhealth.homefragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hidenobi.myhealth.data.stepcounter.StepCounter
import com.hidenobi.myhealth.data.stepcounter.StepCounterRepository
import com.hidenobi.myhealth.data.waterintake.WaterIntake
import com.hidenobi.myhealth.data.waterintake.WaterIntakeRepository
import com.hidenobi.myhealth.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(
    private val waterIntakeRepository: WaterIntakeRepository,
    private val stepCounterRepository: StepCounterRepository
) : ViewModel() {

    // format data
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val date = Date()
    private val dateCurrent: String = sdf.format(date)

    // MutableLiveData for the user's daily step count
    private val _listStepCounter: MutableLiveData<MutableList<StepCounter>> = MutableLiveData()
    val listStepCounter: LiveData<MutableList<StepCounter>> = _listStepCounter
    private val _searchStepCounter = MutableLiveData<StepCounter>()
    val searchStepCounter: LiveData<StepCounter> get() = _searchStepCounter
    private val _currentStepCounter = MutableLiveData<StepCounter>()
    val currentStepCounter: LiveData<StepCounter> get() = _currentStepCounter

    private fun getListStepCounter() {
        viewModelScope.launch(Dispatchers.IO) {
            val listTmp = stepCounterRepository.getAllStepCounter()
            _listStepCounter.postValue(listTmp)
        }
    }

    private fun addStepCounter(stepCounter: StepCounter) {
        viewModelScope.launch(Dispatchers.IO) {
            stepCounterRepository.addStepCounter(stepCounter)
        }
    }

    private fun updateStepCounter(stepCounter: StepCounter) {
        viewModelScope.launch(Dispatchers.IO) {
            stepCounterRepository.updateStepCounter(stepCounter)
        }
    }

    fun searchStepCounterByDate() {
        viewModelScope.launch(Dispatchers.IO) {
            val stepCounterTmp = stepCounterRepository.searchByDate(dateCurrent)
            _searchStepCounter.postValue(stepCounterTmp)
        }
    }

    fun setCurrentStepCounter() {
        if (searchStepCounter.value == null) {
            _currentStepCounter.value = StepCounter(dateCurrent, 0)
        } else {
            _currentStepCounter.value = searchStepCounter.value
        }
    }

    // MutableLiveData for the user's sleep time in minutes
    private val _sleepTime = MutableLiveData(0)
    val sleepTime: LiveData<Int> get() = _sleepTime

    // Method to update the user's sleep time
    fun updateSleepTime(newSleepTime: Int) {
        _sleepTime.value = newSleepTime
    }

    // MutableLiveData for the user's water intake in milliliters

    private val _listWaterIntake: MutableLiveData<MutableList<WaterIntake>> = MutableLiveData()
    val listWaterIntake: LiveData<MutableList<WaterIntake>> = _listWaterIntake
    private val _searchWaterIntake = MutableLiveData<WaterIntake>()
    private val searchWaterIntake: LiveData<WaterIntake> get() = _searchWaterIntake

    private fun getListWaterIntake() {
        viewModelScope.launch(Dispatchers.IO) {
            val listTmp = waterIntakeRepository.getAllCountWaterIntake()
            _listWaterIntake.postValue(listTmp)
        }
    }

    private fun addCountWaterIntake(waterIntake: WaterIntake) {
        viewModelScope.launch(Dispatchers.IO) {
            waterIntakeRepository.addCountWaterIntake(waterIntake)
        }
    }

    private fun updateWaterIntake(waterIntake: WaterIntake) {
        viewModelScope.launch(Dispatchers.IO) {
            waterIntakeRepository.updateWaterIntake(waterIntake)
        }
    }

    private fun searchByDate(searchDateCountWaterIntake: String) {
        val waterIntakeTmp = waterIntakeRepository.searchByDate(searchDateCountWaterIntake)
        _searchWaterIntake.value = waterIntakeTmp

    }

    private val _waterIntake = MutableLiveData<WaterIntake>()
    private val waterIntake: LiveData<WaterIntake> get() = _waterIntake

    // Method to increment or decrease the user's water intake
    private fun incrementWaterIntake() {
        _waterIntake.value?.countWaterIntake = _waterIntake.value?.countWaterIntake?.plus(1)!!
        waterIntake.value?.let { updateWaterIntake(it) }
    }

    private fun decreaseWaterIntake() {
        if (_waterIntake.value?.countWaterIntake!! > 0) {
            _waterIntake.value?.countWaterIntake = _waterIntake.value?.countWaterIntake?.plus(-1)!!
            waterIntake.value?.let { updateWaterIntake(it) }
        }
    }

    // Method to bind the ViewModel to the fragment's layout
    fun bind(binding: FragmentHomeBinding) {
        binding.apply {

//            set up daily waterIntake
            searchByDate(dateCurrent)
            val newWaterIntake = WaterIntake(dateCurrent, 0)
            if (searchWaterIntake.value == null) {
                addCountWaterIntake(newWaterIntake)
                _waterIntake.value = newWaterIntake
            } else {
                _waterIntake.value = searchWaterIntake.value
            }
            tvNumberOfGlassWater.text = waterIntake.value!!.countWaterIntake.toString()

//            update View WaterIntake
            tvPlusWater.setOnClickListener {
                incrementWaterIntake()
                tvNumberOfGlassWater.text = waterIntake.value!!.countWaterIntake.toString()
            }
            tvSubtractWater.setOnClickListener {
                decreaseWaterIntake()
                tvNumberOfGlassWater.text = waterIntake.value!!.countWaterIntake.toString()
            }


//            go to StepCountingScreen
            tvNumOfSteps.setOnClickListener {
                goToStepCountingScreen()
            }
            tvNumberOfSteps.setOnClickListener {
                goToStepCountingScreen()
            }

//            go to SleepCountingScreen
            tvNumOfSleeps.also {
                it.setOnClickListener {
                    goToSleepCountingScreen()
                }
            }

//            go to KcalCountingScreen
            tvNumOfKcal.also {
                it.setOnClickListener {
                    goToKcalCountingScreen()
                }
            }

//            go to DailyScreen
            constraintDaily.setOnClickListener {
                goToDailyScreen()
            }
        }
    }

    private fun goToDailyScreen() {
        Log.i("goToDailyScreen", "not already")
    }

    private fun goToKcalCountingScreen() {
        Log.i("goToKcalCountingScreen", "not already")
    }

    private fun goToSleepCountingScreen() {
        Log.i("goToSleepCountingScreen", "not already")
    }

    private fun goToStepCountingScreen() {
        Log.i("goToStepCountingScreen", "not already")
    }


}