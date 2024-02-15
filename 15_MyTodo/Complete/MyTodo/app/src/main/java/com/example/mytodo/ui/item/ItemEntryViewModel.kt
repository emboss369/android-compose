package com.example.mytodo.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mytodo.data.Item
import com.example.mytodo.data.ItemsRepository



/**
 * ビューモデルを使用して、ルームデータベースにアイテムを検証および挿入します。
 */
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

  // 現在のアイテムの状態を保持する
  var itemUiState by mutableStateOf(ItemUiState())
    private set

  // 引数で指定された値で"itemUiState"を更新する。
  // このメソッドは、入力値のバリデーションもトリガーします。
  fun updateUiState(itemDetails: ItemDetails) {
    itemUiState =
      ItemUiState(
        itemDetails = itemDetails,
        isEntryValid = validateInput(itemDetails))
  }

  suspend fun saveItem() {
    if (validateInput()) {
      itemsRepository.insertItem(itemUiState.itemDetails.toItem())
    }
  }

  private fun validateInput(
    uiState: ItemDetails = itemUiState.itemDetails
  ): Boolean {
    return with(uiState) {
      title.isNotBlank() && description.isNotBlank()
    }
  }
}
// ItemのUi Stateを表します。
data class ItemUiState(
  val itemDetails: ItemDetails = ItemDetails(),
  val isEntryValid: Boolean = false
)

data class ItemDetails(
  val id: Int = 0,
  val title: String = "",
  val description: String = "",
  val done: Boolean = false,
)

/**
 * [ItemUiState]を[Item]に変換する拡張関数。
 */
fun ItemDetails.toItem(): Item = Item(
  id = id,
  title = title,
  description = description,
  done = done
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
  itemDetails = this.toItemDetails(),
  isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
  id = id,
  title = title,
  description = description,
  done = done
)
