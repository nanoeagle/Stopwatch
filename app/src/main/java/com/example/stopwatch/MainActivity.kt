package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val RUNNING_KEY = "running"
    private val OFFSET_KEY = "offset"
    private val BASE_KEY = "base"

    private lateinit var binding: ActivityMainBinding
    private var running = false
    private var offset: Long = 0

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(OFFSET_KEY, offset)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reinstateStopwatchIfSaved(savedInstanceState)
        setListenersForButtons()
    }

    private fun reinstateStopwatchIfSaved(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            offset = savedInstanceState.getLong(OFFSET_KEY)

            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else setBaseTime()
        }
    }

    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun setListenersForButtons() {
        binding.startBtn.setOnClickListener { runStopwatchIfNotRunning() }
        binding.pauseBtn.setOnClickListener { pauseStopwatchIfRunning() }
        binding.resetBtn.setOnClickListener { resetStopwatch() }
    }

    private fun runStopwatchIfNotRunning() {
        if (!running) {
            setBaseTime()
            binding.stopwatch.start()
            running = true
        }
    }

    private fun pauseStopwatchIfRunning() {
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
            running = false
        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    private fun resetStopwatch() {
        offset = 0
        setBaseTime()
        binding.stopwatch.stop()
        if (running) running = false
    }

    override fun onResume() {
        super.onResume()
        resumeStopwatchIfRunning()
    }

    private fun resumeStopwatchIfRunning() {
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
        }
    }

    override fun onPause() {
        super.onPause()
        interruptStopwatchIfRunning()
    }

    private fun interruptStopwatchIfRunning() {
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }
}