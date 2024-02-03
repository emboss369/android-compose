package com.example.postmystory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.postmystory.ui.theme.PostMyStoryTheme

class MainActivity : ComponentActivity() {

  enum class Scene {
    LIST, PHOTOS, CAPTION
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val messages = remember { mutableStateListOf<Message>() }
      var scene by remember { mutableStateOf(Scene.LIST) }
      var selectUrl by remember { mutableStateOf("") }
      var caption by remember { mutableStateOf("") }
      PostMyStoryTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          when (scene) {
            Scene.LIST -> {
              ListScreen(messages = messages, onClick = {
                scene = Scene.PHOTOS
              })
            }

            Scene.PHOTOS -> {
              PhotoGridScreen(onClick = { url ->
                selectUrl = url
                scene = Scene.CAPTION
              })
            }

            Scene.CAPTION -> {
              CaptionScreen(selectUrl = selectUrl, onClick = {
                messages.add(
                  Message(
                    image = selectUrl, caption = caption, nice = 0
                  )
                )
                scene = Scene.LIST
              }, onChange = { newText ->
                caption = newText
              })
            }
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!", modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  PostMyStoryTheme {
    Greeting("Android")
  }
}

@Composable
fun CoilTest() {
  AsyncImage(
    model = "https://developer.android.com/static/images/brand/Android_Robot.png",
    contentDescription = null
  )
}