package com.example.janken

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}


@Composable
fun PlayerView(hand: Int) {
    if (hand == 0) {
        Image(
            painter = painterResource(id = R.drawable.gu), contentDescription = null
        )
    } else if (hand == 1) {
        Image(
            painter = painterResource(id = R.drawable.choki), contentDescription = null
        )
    } else if (hand == 2) {
        Image(
            painter = painterResource(id = R.drawable.pa), contentDescription = null
        )
    }
}

@Preview
@Composable
fun PlayerViewPreview() {
    PlayerView(hand = 2)
}

@Composable
fun ComputerView(comHand: Int) {
    when (comHand) {
        0 -> Image(
            painter = painterResource(id = R.drawable.com_gu), contentDescription = null
        )

        1 -> Image(
            painter = painterResource(id = R.drawable.com_choki), contentDescription = null
        )

        2 -> Image(
            painter = painterResource(id = R.drawable.com_pa), contentDescription = null
        )
    }
}

@Preview
@Composable
fun ComputerViewPreview() {
    ComputerView(comHand = 2)
}

@Composable
fun ResultView(result: Int) {
    when (result) {
        0 -> Text(text = "あいこです")
        1 -> Text(text = "あなたの勝ちです")
        2 -> Text(text = "コンピュータの勝ちです")
    }
}

@Preview
@Composable
fun ResultViewPreview() {
    ResultView(result = 2)
}

@Composable
fun Content() {
    var myHand by remember { mutableStateOf(-1) }
    var comHand by remember { mutableStateOf(-1) }
    var result by remember { mutableStateOf(-1) }
    Column {
        Text(text = "じゃんけんアプリ")
        Row {
            Button(onClick = {
                myHand = 0
                comHand = (0..2).random()
                result = (comHand - myHand + 3) % 3
            }) {
                Text(text = "グー")
            }
            Button(onClick = {
                myHand = 1
                comHand = (0..2).random()
                result = (comHand - myHand + 3) % 3
            }) {
                Text(text = "チョキ")
            }
            Button(onClick = {
                myHand = 2
                comHand = (0..2).random()
                result = (comHand - myHand + 3) % 3
            }) {
                Text(text = "パー")
            }
        }

        PlayerView(hand = myHand)
        ResultView(result = result)
        ComputerView(comHand = comHand)
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content()
}
