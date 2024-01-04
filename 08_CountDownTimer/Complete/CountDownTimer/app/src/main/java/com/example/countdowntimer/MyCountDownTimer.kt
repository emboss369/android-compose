package com.example.countdowntimer

import android.os.CountDownTimer

// ## MyCountDownTimer クラスの変数:
// - millisInFuture: タイマーの総時間をミリ秒単位で指定します。これはタイマーの初期設定時に受け取ります。
// - countDownInterval: タイマーの更新間隔をミリ秒単位で指定します。onTickメソッドが呼び出される間隔です。
// - changed: カウントダウン中に指定された間隔ごとに呼び出されるコールバック関数です。残り時間が更新された際にこの関数が呼び出されます。

/**
 * カウントダウンを実行するためのクラスです。
 * カウントダウン中に指定された間隔ごとにコールバック関数を呼び出します。
 * カウントダウンが終了した際にコールバック関数を呼び出します。
 */
class MyCountDownTimer(
  millisInFuture: Long,
  countDownInterval: Long,
  val changed: (Long) -> Unit,
  val finished: () -> Unit
) : CountDownTimer(millisInFuture, countDownInterval) {

  override fun onTick(millisUntilFinished: Long) {
    changed(millisUntilFinished)
  }

  override fun onFinish() {
    finished()
  }
}