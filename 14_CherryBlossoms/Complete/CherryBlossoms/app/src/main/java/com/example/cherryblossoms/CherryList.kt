package com.example.cherryblossoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CherryList(
  cherryList: List<Cherry>, onSelected: (Cherry) -> Unit = {}
) {
  var searchQuery by remember { mutableStateOf("") }
  val filteredList = cherryList.filter {
    it.name.contains(searchQuery, ignoreCase = true)
  }
  val scope = rememberCoroutineScope()
  Column {
    TextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      label = { Text("Search") },
      modifier = Modifier.fillMaxWidth()
    )
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {

      items(filteredList.size) { index ->
        CherryRow(filteredList[index]) { cherry ->
          scope.launch {
            onSelected(cherry)
          }
        }
      }
    }
  }
}
