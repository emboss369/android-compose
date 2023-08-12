package com.example.mytodo.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodo.data.ItemsRepository
import kotlinx.coroutines.launch

//
/**
 * [ItemsRepository] のデータソースからアイテムを取得・更新する ViewModel です。
 */
open class ItemEditViewModel(
  savedStateHandle: SavedStateHandle,
  private val itemsRepository: ItemsRepository
) : ViewModel() {
  /**
   * 現在のアイテムの状態を保持する
   */
  var itemUiState by mutableStateOf(ItemUiState())
    private set

  private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

  // 初期化ブロック(init)によって初期化処理を行う
  init {
    viewModelScope.launch {
      itemUiState = itemsRepository.getItemStream(itemId).toItemUiState(true)
    }
  }

  /**
   * Update the item in the [ItemsRepository]'s data source
   */
  suspend fun updateItem() {
    if (validateInput(itemUiState.itemDetails)) {
      itemsRepository.updateItem(itemUiState.itemDetails.toItem())
    }
  }

  // 引数で指定された値で"itemUiState"を更新する。
  // このメソッドは、入力値のバリデーションもトリガーします。
  fun updateUiState(itemDetails: ItemDetails) {
    itemUiState =
      ItemUiState(
        itemDetails = itemDetails,
        isEntryValid = validateInput(itemDetails))
  }

  private fun validateInput(
    uiState: ItemDetails = itemUiState.itemDetails
  ): Boolean {
    return with(uiState) {
      title.isNotBlank() && description.isNotBlank()
    }
  }
}
