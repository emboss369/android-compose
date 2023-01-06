package com.example.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animation.ui.theme.AnimationTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


enum class ButtonState { Pressed, Idle }
fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.8f else 1f)

//    val radius by animateFloatAsState(if (buttonState == ButtonState.Pressed) 100.0f else 0f)
//    val alpha by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.5f else 0f)

    //val interactionSource = remember { MutableInteractionSource() }
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        // pointerInput修飾子でタップ操作を検知できます。
        .pointerInput(Unit) {
            // compose1.4からは、forEachGestureは非推奨となり、かわりにawaitEachGesture()を使います。
            // forEachGesture/awaitEachGestureは、ジェスチャーを繰り返し処理することができます。
            forEachGesture {
                awaitPointerEventScope {
                    // 指がボタンを押し下げるのを待ちます。
                    awaitFirstDown(requireUnconsumed = false)
                    // タップされたら押されたアニメーション開始します
                    buttonState = ButtonState.Pressed
                    // 指がボタンから離れるのを待ちます。
                    waitForUpOrCancellation()
                    // 指が離れたら、ボタンを元の状態にします。
                    buttonState = ButtonState.Idle
                }
            }
        }
    // drawWithContentを使うと、コンテンツの上にレンダリング出来る。
//        .drawWithContent {
//            drawContent()
//            if (buttonState == ButtonState.Pressed)
//                drawRect(
//                    Brush.radialGradient(
//                        listOf(Color.Transparent, Color.Black),
//                        center = Offset(100f, 100f),
//                        radius = 100.dp.toPx(),
//                    )
//                )
//            drawCircle(color = Color.Yellow, radius = radius, alpha = alpha)
//        }
}

@Composable
fun AnimButton() {

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Button(
                onClick = {

                }, shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.bounceClick()
            ) {
                Text(text = "Click me")
            }

        }
    }

}
@Preview(showBackground = true)
@Composable
fun AnimButtonPreview() {
    AnimationTheme {
        AnimButton()
    }
}