package com.example.postmystory.example

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.postmystory.ui.theme.PostMyStoryTheme

@Composable
fun ColumnExample(name: String) {
  LazyColumn {
    item {
      Text(text = "Hello $name!")
    }
    items(5) { index ->
      Text(text = "Item: $index")
    }
    item {
      Text(text = "Last item")
    }
  }
}

@Preview
@Composable
fun ColumnExamplePreview() {
  PostMyStoryTheme {
    ColumnExample("Android")
  }
}