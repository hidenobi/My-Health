package com.hidenobi.myhealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hidenobi.myhealth.R
import com.hidenobi.myhealth.data.stepcounter.StepCounter
import com.hidenobi.myhealth.data.stepcounter.StepCounterDao
import com.hidenobi.myhealth.data.stepcounter.StepCounterDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounterSensor: Sensor

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var stepCounterDao: StepCounterDao
    private lateinit var stepCounterDatabase: StepCounterDatabase

    // format data
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var date = Date()
    private var dateCurrent: String = sdf.format(date)

    // Notification channel ID and name
    private val channelId = "StepCounterChannel"
    private val channelName = "Step Counter"

    // Notification ID
    private val notificationId = 1

    private var currentStepCounter: StepCounter = StepCounter(dateCurrent, 0)

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCounterDatabase = StepCounterDatabase.getDatabase(applicationContext)
        stepCounterDao = stepCounterDatabase.stepCounterDao()

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)

        // Create the notification for the foreground service
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("My health")
            .setContentText("Counting steps...")
            .setSmallIcon(R.drawable.ic_steps)
            .build()

        // Start the foreground service
        startForeground(notificationId, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        scope.launch {
            withContext(Dispatchers.IO) {
                date = Date()
                dateCurrent = sdf.format(date)
                currentStepCounter = stepCounterDao.searchByDate(dateCurrent)
                if (currentStepCounter == null) {
                    currentStepCounter = StepCounter(dateCurrent, 0)
                }
                stepCounterDao.addStepCounter(currentStepCounter)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        scope.launch {
            withContext(Dispatchers.IO) {
                currentStepCounter.stepCounter++
                stepCounterDao.updateStepCounter(currentStepCounter)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}