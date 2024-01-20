package com.example.myanimation.sample

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimationBoxSize(animationSpec: AnimationSpec<Float>, text: String) {
  var size by remember { mutableStateOf(0.5f) }
  val animationScale by animateFloatAsState(
    targetValue = size, animationSpec = animationSpec
  )
  Box(
    modifier = Modifier.size(size = 300.dp), contentAlignment = Alignment.Center
  ) {
    Box(modifier = Modifier
      .scale(scale = animationScale)
      .size(size = 100.dp)
      .background(color = Color.Magenta)
      .clickable {
        size = if (size == 2f) 0.5f else 2f
      }) {
      Text(text = text)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimationBoxSizePreview() {
  Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
    AnimationBoxSize(
      spring(
        // dampingRatio は、ばねの弾性を定義します。デフォルト値は Spring.DampingRatioNoBouncy です。
        dampingRatio = Spring.DampingRatioHighBouncy,
        // stiffness は、終了値までのばねの移動速度を定義します。デフォルト値は Spring.StiffnessMedium です。
        stiffness = Spring.StiffnessMedium
      ), "ばねアニメーション"
    )
    AnimationBoxSize(
      // トゥイーンアニメーション
      // イージング カーブを使用して、指定された時間で目標値に向かってアニメーション化します。
      tween(
        // アニメーションの継続時間
        durationMillis = 2000,
        // アニメーションの開始を延期する。
        delayMillis = 40,
        // 開始と終了の間を補間するために使用されるイージングカーブ
        easing = FastOutLinearInEasing
      ), "トゥイーンアニメーション"
    )
    AnimationBoxSize(
      // 繰り返しアニメーション
      // 初期値とターゲットの間で繰り返すアニメーションを作成します。
      repeatable(
        // iterations - 反復の合計数．繰り返す場合は，1より大きくなければなりません．
        iterations = 3,
        //  animation - 繰り返し再生されるアニメーションです。
        animation = tween(durationMillis = 500),
        // repeatMode - アニメーションを最初から繰り返すか（ RepeatMode.Restart など）、最後から繰り返すか（ RepeatMode.Reverse など）です。
        repeatMode = RepeatMode.Reverse
        // initialStartOffset - アニメーションの開始をオフセットします。
      ), "繰り返しアニメーション"
    )
    AnimationBoxSize(
      // 無限繰り返しアニメーション
      // 繰り返しアニメーションと同じですが無限に繰り返します。
      infiniteRepeatable(
        // animation - 繰り返し再生されるアニメーションです。
        // repeatMode - アニメーションを最初から繰り返すか（ RepeatMode.Restart など）、最後から繰り返すか（ RepeatMode.Reverse など）です。
        // initialStartOffset - アニメーションの開始をオフセットします。
        animation = tween(durationMillis = 500),
        repeatMode = RepeatMode.Reverse
      ),
      "無限繰り返しアニメーション"
    )
    AnimationBoxSize(

// https://developer.android.com/jetpack/compose/animation?hl=ja#keyframesß
// keyframes
//keyframes は、アニメーションの持続時間内の異なるタイムスタンプで指定されたスナップショット値に基づいてアニメーション化します。アニメーション値は、常に 2 つのキーフレーム値の間で補間されます。これらのキーフレームごとに、イージングを指定して補間曲線を設定できます。

      keyframes {
        durationMillis = 1000
        0.0f at 0 with LinearOutSlowInEasing // for 0-750 ms
        1.5f at 750 with FastOutLinearInEasing // for 750-100 ms
        2.0f at 1000
      },
      "キーフレームアニメーション"
    )
    AnimationBoxSize(
      //snap
//snap は、値をすぐに終了値に切り替える特別な AnimationSpec です。delayMillis を指定して、アニメーションの開始を遅らせることができます。

      //アニメーションする値をすぐに終了値に切り替えるための
      // Snapアニメーションを作成する。
      //パラメータは以下のとおりです。
      //delayMillis - アニメーションが実行されるまでの
      // 待ち時間（ミリ秒）です。デフォルトでは0です。
      snap(
        delayMillis = 1000
      ),
      "スナップアニメーション"
    )

  }
}
/**
Compose には、Float、Color、Dp、Size、Offset、Rect、Int、IntOffset、IntSize 用の animate*AsState 関数が、すぐに使える状態で用意されています。
animateFloatAsState
animateColorAsState
animateDpAsState
animateSizeAsState
animateOffsetAsState
animateRectAsState
animateIntAsState
animateIntOffsetAsState
animateIntSizeAsState
 */