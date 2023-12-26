package com.example.countdowntimer

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// https://developer.android.com/jetpack/compose/state?hl=ja#viewmodels-source-of-truth
// を参考に、ViewModel作成する。

// State class for the UI of the Example screen (ExampleActivity)
// このクラスは、UIの状態を保持するクラスです。
// このクラスのインスタンスをViewModelに保持させることで、
// ViewModelがUIの状態を保持することができます。
// このクラスは、UIの状態を保持するためのものなので、
// このクラスのインスタンスを変更すると、UIに反映されます。
data class ExampleUiState(
    var time: Long = 3 * 60 * 1000,
    var timeLeft: Long = 3 * 60 * 1000,
    var isRunning: Boolean = false,

)

class MyCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long,
    // countDownIntervalごとに呼ばれる
    val changed: (millisUntilFinished: Long) -> Unit,
    // タイマーが0になったら呼ばれる
    val finished: () -> Unit
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        Log.d("MyCountDownTimer","onTick ${millisUntilFinished}")
        changed(millisUntilFinished)
    }

    override fun onFinish() {
        Log.d("MyCountDownTimer","onFinish")
        finished()
    }
}


class ExampleViewModel() : ViewModel() {

    var uiState by mutableStateOf(ExampleUiState())
        private set // setterをprivateにして外部から使用できなく

    var timer: MyCountDownTimer? = null
    //var time: Long = 0 // セットしたタイマー時間（ミリ秒）

    // Business logic
    fun startTimer(millisInFuture: Long) {
        uiState.time = millisInFuture
        Log.d("ExampleViewModel","startTimer")
        uiState.isRunning = true
//        uiState = uiState.copy().apply {
//            isRunning = true
//        }
        timer = MyCountDownTimer(
            millisInFuture = millisInFuture,
            countDownInterval = 100,
            changed = { millisUntilFinished ->
                uiState = ExampleUiState(
                    time = millisInFuture,
                    timeLeft = millisUntilFinished,
                    isRunning = true,

                // https://qiita.com/mimimi-no-sesese/items/2f9f64333c3339453430
                // Long（整数）の除算はLong（整数）になります。今回は0.0〜1.0の少数（パーセンテージ）でほしいので
                    // .toFloat()して少数になるようにしましょう。
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
        Log.d("ExampleViewModel","stopTimer")
        timer?.cancel()
        uiState = ExampleUiState(
            time = uiState.time,
            timeLeft = 0,
            isRunning = false
        )
    }
}