package com.example.countdowntimer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// ## ExampleViewModel クラスの変数:
// - uiState: UIの状態を管理するための ExampleUiState クラスのインスタンスです。
// Composeにおいて、この変数が変更されるとUIが更新されます。
// - timer: カウントダウンを実行するための MyCountDownTimer クラスのインスタンスです。
// ## メソッド:
// - startTimer(millisInFuture: Long): タイマーを開始するメソッドです。
// 指定された時間のカウントダウンを開始し、UIの状態を更新します。
// MyCountDownTimer を作成して開始しています。
// - stopTimer(): タイマーを停止するメソッドです。
// タイマーが停止されると、UIの状態が更新され、残り時間が表示されます。
// - addTime(second: Int): タイマーに指定された秒数を追加するメソッドです。
/**
 * UIの状態を管理するためのクラスです。
 * Composeにおいて、このクラスのインスタンスが変更されるとUIが更新されます。
 */
class ExampleViewModel() : ViewModel() {

  var uiState by mutableStateOf(ExampleUiState())

  var timer: MyCountDownTimer? = null

  fun startTimer(millisInFuture: Long) {
    uiState.time = millisInFuture
    uiState.isRunning = true
    timer = MyCountDownTimer(
      millisInFuture = millisInFuture,
      countDownInterval = 100,
      changed = { millisUntilFinished ->
        uiState = ExampleUiState(
          time = millisInFuture,
          timeLeft = millisUntilFinished,
          isRunning = true,
        )
      },
      finished = {
        uiState = ExampleUiState(
          time = millisInFuture,
          timeLeft = 0,
          isRunning = false
        )
      }
    )
    timer?.start()
  }

  fun stopTimer() {
    timer?.cancel()
    uiState = ExampleUiState(
      time = 3 * 60 * 1000,
      timeLeft = 3 * 60 * 1000,
      isRunning = false
    )
  }

  fun addTime(second: Int) {
    if (!uiState.isRunning) {
      val newTime = uiState.time + second * 1000L
      uiState = ExampleUiState(
        time = newTime,
        timeLeft = newTime,
        isRunning = false
      )
    }
  }
}