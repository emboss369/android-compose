package com.example.countdowntimer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

/**
 * 画面の真ん中に、円弧を描画する。
 * @param color 円弧の色
 * @param timeLeft 残り時間。0.0fから1.0fまでの値をとる。
 */
@Composable
fun Arc(color: Color, timeLeft: Float) {
  Canvas(
    modifier = Modifier
      .fillMaxSize()
  ) {
    val strokeWidth = min(size.width, size.height) / 20.0f
    val minSize = min(size.width, size.height) - strokeWidth
    val maxSize = max(size.width, size.height)
    var offset: Offset
    if (size.width < size.height) {
      offset = Offset(strokeWidth / 2.0f, (maxSize - minSize) / 2.0f )
    } else {
      offset = Offset((maxSize - minSize) / 2.0f, strokeWidth / 2.0f )
    }
    drawArc(
      color = color,
      startAngle = -90.0f,
      sweepAngle = timeLeft * 360.0f,
      useCenter = false,
      topLeft = offset,
      size = Size(minSize, minSize),
      style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
  }
}

@Preview(widthDp = 150, heightDp = 100)
@Composable
fun ArcPreview1() {
  Arc(color = Color.Green, timeLeft = 1.0f)
}
@Preview(widthDp = 200, heightDp = 300)
@Composable
fun ArcPreview2() {
  Arc(color = Color.Green, timeLeft = 0.75f)
}
@Preview(widthDp = 500, heightDp = 500)
@Composable
fun ArcPreview3() {
  Arc(color = Color.Green, timeLeft = 0.5f)
}