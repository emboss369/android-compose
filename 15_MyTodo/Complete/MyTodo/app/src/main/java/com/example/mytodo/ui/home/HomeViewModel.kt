package com.example.mytodo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.Item
import com.example.mytodo.data.ItemsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {


  //  val homeUiState: StateFlow<HomeUiState> =
//    // とりあえずデバッグ用に固定のデータを作成。
//    HomeUiState(itemsRepository.getAllItemsStream())
//  .stateIn(
//    scope = viewModelScope,
//    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//    initialValue = HomeUiState()
//  )
  val homeUiState = HomeUiState(itemsRepository.getAllItemsStream())

//  init {
//    viewModelScope.launch {
//      homeUiState = HomeUiState(itemsRepository.getAllItemsStream())
//    }
//  }

}


data class HomeUiState(val itemList: Flow<List<Item>> = flowOf(listOf()))