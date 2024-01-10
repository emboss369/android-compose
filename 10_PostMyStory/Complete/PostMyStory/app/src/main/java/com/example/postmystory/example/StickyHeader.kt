package com.example.postmystory.example

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.postmystory.ui.theme.PostMyStoryTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeader() {
  val sections = listOf("A", "B", "C", "D", "E", "F", "G")
  LazyColumn {
    sections.forEach { section ->
      stickyHeader {
        Text(
          text = section,
          // onPrimary は MaterialTheme に含まれる色の一つで、プライマリカラー上に表示されるテキストとアイコンに使用される色。
          color = MaterialTheme.colorScheme.onPrimary,
          // headlineLarge は MaterialTheme に含まれるテキストスタイルの一つで、大きな見出しに使用される。
          style = MaterialTheme.typography.headlineLarge,
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
        )
      }
      items(10) { index ->
        Text(
          text = "Item: $index from section $section",
          modifier = Modifier.padding(16.dp),
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun StickyHeaderPreview() {
  PostMyStoryTheme {
    StickyHeader()
  }
}