package com.example.postmystory

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PhotoGridScreen(onClick: (String) -> Unit) {
  val photos = mutableListOf<String>().apply {
    for(i in 1..24) {
      add("https://picsum.photos/seed/${i}/200")
    }
  }
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 128.dp)
  ) {

    items(items = photos) { photo ->
      PhotoItem(photo, onClick)
    }
  }
}
@Preview(showBackground = true)
@Composable
fun PhotoGridScreenPreview() {
  PhotoGridScreen(){}
}