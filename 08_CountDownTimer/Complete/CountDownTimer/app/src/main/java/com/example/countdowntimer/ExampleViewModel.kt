package com.example.countdowntimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExampleViewModel : ViewModel() {
  var uiState by mutableStateOf(ExampleUiState())
    private set
  var timer: MyCountDownTimer? = null

  fun startTimer(millisInFuture: Long) {
    uiState.time = millisInFuture
    uiState.isRunning = true
    timer = MyCountDownTimer(millisInFuture = millisInFuture,
      countDownInterval = 100L,
      changed = { millisUntilFinished ->
        uiState = ExampleUiState(
          time = millisInFuture,
          timeLeft = millisUntilFinished,
          isRunning = true
        )
      },
      finished = {
        uiState = ExampleUiState(
          time = millisInFuture, timeLeft = 0L, isRunning = false
        )
      })
    timer?.start()
  }

  fun stopTimer() {
    timer?.cancel()
    uiState = ExampleUiState(
      time = 3 * 60 * 1000L, timeLeft = 3 * 60 * 1000L, isRunning = false
    )
  }

  fun addTime(second: Int) {
    if (!uiState.isRunning) {
      val newTime = uiState.time + second * 1000L
      uiState = ExampleUiState(
        time = newTime, timeLeft = newTime, isRunning = false
      )
    }
  }
}