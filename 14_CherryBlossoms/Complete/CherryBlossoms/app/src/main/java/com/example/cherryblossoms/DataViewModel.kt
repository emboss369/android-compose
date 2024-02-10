package com.example.cherryblossoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cherryblossoms.data.ApiResult
import fuel.Fuel
import fuel.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DataViewModel : ViewModel() {
  var uiState = MutableStateFlow(UIState())

  fun query() {
    viewModelScope.launch {
      val string = Fuel.get(
        """
        https://ja.wikipedia.org/w/api.php?
        action=query&format=json&prop=images&list=search&formatversion=2&
        imlimit=1&srsearch=桜の名所&srlimit=500
        """.trimIndent().replace(System.lineSeparator(), "")
      ).body
      val result =
        Json { ignoreUnknownKeys = true }.decodeFromString<ApiResult>(string)
      uiState.value = UIState(result.query.search)
    }
  }
}