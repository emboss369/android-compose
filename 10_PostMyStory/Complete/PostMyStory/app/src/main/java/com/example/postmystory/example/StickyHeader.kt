package com.example.postmystory.example

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeader() {
  val sections = listOf("A", "B", "C", "D", "E", "F", "G")
  LazyColumn {
    sections.forEach { section ->
      stickyHeader {
        Text(
          text = section,
          color = MaterialTheme.colorScheme.onPrimary,
          style = MaterialTheme.typography.headlineLarge,
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
        )
      }
      items(10) { index ->
        Text(
          text = "Item : $index from $section",
          modifier = Modifier.padding(16.dp),
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Preview
@Composable
fun StickyHeaderPreview() {
  StickyHeader()
}