package com.example.cherryblossoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cherryblossoms.data.ApiSearch

@Composable
fun WikipediaView(
  viewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
  onSelected: (ApiSearch) -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()
  if (uiState.CherryList == null || uiState.CherryList?.isEmpty() == true) {
    viewModel.query()
  }
  if (uiState.CherryList == null) {
    Box(
      modifier = Modifier
        .padding(64.dp)
        .fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      LinearProgressIndicator()
    }
  } else {
    WikipediaList(list = uiState.CherryList, onSelected = onSelected)
  }
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    LinearProgressIndicator()
}