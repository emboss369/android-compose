package com.example.mytodo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.Item
import com.example.mytodo.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(itemsRepository: ItemsRepository): ViewModel() {


  val homeUiState: StateFlow<HomeUiState> = flow {
    // とりあえずデバッグ用に固定のデータを作成。
    emit(HomeUiState(itemsRepository.getAllItemsStream()))
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    initialValue = HomeUiState()
  )

  companion object {
    private const val TIMEOUT_MILLIS = 5_000L
  }
}


data class HomeUiState(val itemList: List<Item> = listOf())