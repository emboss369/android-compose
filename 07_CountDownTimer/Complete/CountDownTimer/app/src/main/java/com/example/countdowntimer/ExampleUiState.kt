package com.example.countdowntimer

// ## ExampleUiState データクラスの変数:
// - time: タイマーの初期時間（分）を表します。初期値は3分です。
// - timeLeft: タイマーの残り時間（分）を表します。
// - isRunning: タイマーが実行中かどうかを示すブール値です。
/**
 * UIの状態を管理するためのデータクラスです。
 * Composeにおいて、このクラスのインスタンスが変更されるとUIが更新されます。
 */
data class ExampleUiState(
  var time: Long = 3 * 60 * 1000,
  var timeLeft: Long = 3 * 60 * 1000,
  var isRunning: Boolean = false,
)