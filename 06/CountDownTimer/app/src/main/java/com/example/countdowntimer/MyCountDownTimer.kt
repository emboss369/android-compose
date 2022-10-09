package com.example.countdowntimer

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// https://developer.android.com/jetpack/compose/state?hl=ja#viewmodels-source-of-truth
// を参考に、ViewModel作成する。

data class ExampleUiState(
    var minute: Long = 3,   // varで変更して、Stateの変更はUIに反映されるのか？それとも反映されないのか？
    var second: Long = 0,
    var isRunning: Boolean = false,
    var timeLeft: Float = 1.0f // 円グラフ用、残り時間の割合です
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
    var time: Long = 0 // セットしたタイマー時間（ミリ秒）

    // Business logic
    fun startTimer(millisInFuture: Long) {
        time = millisInFuture
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
                    minute = millisUntilFinished / 1000L / 60L,
                    second = millisUntilFinished / 1000L % 60L,
                    isRunning = true,
                    timeLeft = millisUntilFinished.toFloat() / time.toFloat()
                // https://qiita.com/mimimi-no-sesese/items/2f9f64333c3339453430
                // Long（整数）の除算はLong（整数）になります。今回は0.0〜1.0の少数（パーセンテージ）でほしいので
                    // .toFloat()して少数になるようにしましょう。
                )
            },
            finished = {
                uiState = ExampleUiState(
                    minute = 0,
                    second = 0,
                    isRunning = false,
                    timeLeft = 0f
                )
            }
        )
        timer?.start()

    }

    fun stopTimer() {
        Log.d("ExampleViewModel","stopTimer")
        timer?.cancel()
        uiState = ExampleUiState(
            minute = time / 1000L / 60L,
            second = time / 1000L % 60L,
            isRunning = false,
            timeLeft = 1f
        )
    }
}