package com.example.mytodo.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodo.R
import com.example.mytodo.ui.AppViewModelProvider
import com.example.mytodo.ui.TodoTopAppBar
import com.example.mytodo.ui.navigation.NavigationDestination
import com.example.mytodo.ui.theme.MyTodoTheme
import kotlinx.coroutines.launch

object ItemEntryDestination : NavigationDestination {
  override val route = "item_entry"
  override val titleRes = R.string.item_entry_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
  navigateBack: () -> Unit,
  onNavigateUp: () -> Unit,
  canNavigateBack: Boolean = true,
  viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
  val coroutineScope = rememberCoroutineScope()
  Scaffold(
    topBar = {
      TodoTopAppBar(
        title = stringResource(ItemEntryDestination.titleRes),
        canNavigateBack = canNavigateBack,
        navigateUp = onNavigateUp
      )
    }
  ) { innerPadding ->
    ItemEntryBody(
      itemUiState = viewModel.itemUiState,
      onItemValueChange = viewModel::updateUiState,
      onSaveClick = {
        coroutineScope.launch {
          viewModel.saveItem()
          navigateBack()
        }
      },
      modifier = Modifier
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()
    )
  }
}

// ui/item/ItemEntryScreen.ktファイルでは、ItemEntryBody()コンポーザブルは、ステータ・コードの一部として部分的に実装されています。ItemEntryScreen()関数呼び出しの中のItemEntryBody()コンポーザブルを見てください。
// UI状態とupdateUiStateラムダが関数のパラメータとして渡されていることに注意してほしい。関数の定義を見て、UI状態がどのように更新されているかを確認しよう。
@Composable
fun ItemEntryBody(
  itemUiState: ItemUiState,
  onItemValueChange: (ItemDetails) -> Unit,
  onSaveClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
    modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
  ) {
    ItemInputForm(
      itemDetails = itemUiState.itemDetails,
      onValueChange = onItemValueChange,
      modifier = Modifier.fillMaxWidth()
    )
    // 保存ボタン
    Button(
      onClick = onSaveClick,
      enabled = itemUiState.isEntryValid,
      shape = MaterialTheme.shapes.small,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = stringResource(R.string.save_action))
    }
  }

}


// temInputForm()コンポーザブル関数の実装を見て、
// onValueChange関数パラメータに注目してください。
// ユーザーがテキストフィールドに入力した値でitemDetailsの値を更新しています。
// Saveボタンが有効になるまでに、itemUiState.itemDetailsには保存が必要な値があります。
@Composable
fun ItemInputForm(
  itemDetails: ItemDetails,
  modifier: Modifier = Modifier,
  onValueChange: (ItemDetails) -> Unit = {}
  // enabledで、編集可能、不可能を制御
  // ,enabled: Boolean = true
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
  ) {
    // マテリアルデザインの outlined text field.
    OutlinedTextField(
      value = itemDetails.title,
      onValueChange = { onValueChange(itemDetails.copy(title = it)) },
      label = { Text(stringResource(R.string.todo_name_req)) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
      ),
      modifier = Modifier.fillMaxWidth(),
      // enabled - このテキストフィールドの有効状態を制御します。
      // Falseの場合、このコンポーネントはユーザ入力に応答せず、
      // 視覚的に無効となり、アクセシビリティサービスには無効と表示されます。
      // enabled = enabled,
      singleLine = true
    )
    OutlinedTextField(
      value = itemDetails.description,
      onValueChange = { onValueChange(itemDetails.copy(description = it)) },
      label = { Text(stringResource(R.string.todo_desc_req)) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
      ),
      modifier = Modifier.fillMaxWidth(),
      // enabled = enabled,
      singleLine = true
    )
    Row(
      Modifier
        .fillMaxWidth()
        .height(56.dp)
        .toggleable(
          value = itemDetails.done,
          onValueChange = { onValueChange(itemDetails.copy(done = it)) },
          role = Role.Checkbox
        )
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(
        checked = itemDetails.done,
        onCheckedChange = null // スクリーンリーダーでのアクセシビリティのためにNULLを推奨
      )
      Text(
        text = stringResource(R.string.done),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(start = 16.dp)
      )
    }
    //if (enabled) {
    Text(
      text = stringResource(R.string.required_fields),
      modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
    )
    //}
  }

}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
  MyTodoTheme {
    ItemEntryBody(itemUiState = ItemUiState(
      ItemDetails(
        title = "Item name", description = "10.00"
      )
    ), onItemValueChange = {}, onSaveClick = {})
  }
}