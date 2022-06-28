package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    private val RUNNING_KEY = "running"
    private val OFFSET_KEY = "offset"
    private val BASE_KEY = "base"

    private lateinit var stopwatch: Chronometer
    private var running = false
    private var offset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopwatch = findViewById(R.id.stopwatch)

        reinstateStopwatchIfSaved(savedInstanceState)
        setListenersForButtons()
    }

    private fun reinstateStopwatchIfSaved(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            offset = savedInstanceState.getLong(OFFSET_KEY)

            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun setListenersForButtons() {
        val startBtn = findViewById<Button>(R.id.start_btn)
        startBtn.setOnClickListener { runStopwatchIfNotRunning() }

        val pauseBtn = findViewById<Button>(R.id.pause_btn)
        pauseBtn.setOnClickListener { pauseStopwatchIfRunning() }

        val resetBtn = findViewById<Button>(R.id.reset_btn)
        resetBtn.setOnClickListener { resetStopwatch() }
    }

    private fun runStopwatchIfNotRunning() {
        if (!running) {
            setBaseTime()
            stopwatch.start()
            running = true
        }
    }

    private fun pauseStopwatchIfRunning() {
        if (running) {
            saveOffset()
            stopwatch.stop()
            running = false
        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    private fun resetStopwatch() {
        offset = 0
        setBaseTime()
        stopwatch.stop()
        if (running) running = false;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(OFFSET_KEY, offset)
        outState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(outState)
    }
}