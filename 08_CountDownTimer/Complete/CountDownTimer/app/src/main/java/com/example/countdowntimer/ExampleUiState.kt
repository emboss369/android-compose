package com.example.countdowntimer

data class ExampleUiState(
  var time: Long = 3 * 60 * 1000,
  var timeLeft: Long = time,
  var isRunning: Boolean = false,
)