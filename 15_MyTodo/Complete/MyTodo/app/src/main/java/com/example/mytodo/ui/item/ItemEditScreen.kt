package com.example.mytodo.ui.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodo.R
import com.example.mytodo.ui.AppViewModelProvider
import com.example.mytodo.ui.TodoTopAppBar
import com.example.mytodo.ui.dialog.DeleteItemDialog
import com.example.mytodo.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object ItemEditDestination : NavigationDestination {
  override val route = "item_edit"
  override val titleRes = R.string.edit_item_title
  const val itemIdArg = "itemId"
  val routeWithArgs = "$route/{$itemIdArg}" // TODO:何に使うのか
}
// Todoアイテム編集画面は、Todoアイテム新規入力画面を使いまわして作る方針

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
  navigateBack: () -> Unit = {},
  onNavigateUp: () -> Unit = {},
  modifier: Modifier = Modifier,
  viewModel: ItemEditViewModel = viewModel(
    factory = AppViewModelProvider.Factory
  )
) {
  val coroutineScope = rememberCoroutineScope()
  var showDialog by remember { mutableStateOf(false) }
  Scaffold(
    topBar = {
      TodoTopAppBar(
        title = stringResource(ItemEditDestination.titleRes),
        canNavigateBack = true,
        navigateUp = onNavigateUp
      )
    },
    modifier = modifier
  ) { innerPadding ->
    ItemEntryBody(
      itemUiState = viewModel.itemUiState,
      onItemValueChange = viewModel::updateUiState,
      onSaveClick = {
        coroutineScope.launch {
          viewModel.updateItem()
        }
        navigateBack()
      },
      showDelete = true,
      onDeleteClick = { showDialog = true },
      modifier = Modifier.padding(innerPadding)
    )
  }
  if (showDialog) {
    DeleteItemDialog(
      onConfirm = {
        coroutineScope.launch {
          viewModel.deleteItem()
        }
        navigateBack()
      },
      onDismiss = { showDialog = false }
    )
  }
}