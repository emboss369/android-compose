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
import androidx.compose.material3.ButtonDefaults
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
import com.example.mytodo.TodoTopAppBar
import com.example.mytodo.data.Item
import com.example.mytodo.data.ItemsRepository
import com.example.mytodo.ui.AppViewModelProvider
import com.example.mytodo.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

object ItemEntryDestination : NavigationDestination {
  override val route = "item_entry"
  override val titleRes = R.string.item_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
  navigateBack: () -> Unit = {},
  onNavigateUp: () -> Unit = {},
  canNavigateBack: Boolean = true,
  viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
  val coroutineScope = rememberCoroutineScope()
  Scaffold(topBar = {
    TodoTopAppBar(
      title = stringResource(ItemEntryDestination.titleRes),
      canNavigateBack = canNavigateBack,
      navigateUp = onNavigateUp
    )
  }) { innerPadding ->
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

@Preview(showBackground = true)
@Composable
fun ItemEntryScreenPreview() {
  ItemEntryScreen(
    viewModel = ItemEntryViewModel(itemsRepository = object : ItemsRepository {
      override fun getAllItemsStream(): Flow<List<Item>> = emptyFlow()
      override fun getItemStream(id: Int): Flow<Item?> = emptyFlow()
      override suspend fun insertItem(item: Item) {}
      override suspend fun deleteItem(item: Item) {}
      override suspend fun updateItem(item: Item) {}
    })
  )
}

@Composable
fun ItemInputForm(
  itemDetails: ItemDetails,
  modifier: Modifier = Modifier,
  onValueChange: (ItemDetails) -> Unit = {}
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(
      dimensionResource(id = R.dimen.padding_medium)
    )
  ) {
    OutlinedTextField(
      value = itemDetails.title,
      onValueChange = { onValueChange(itemDetails.copy(title = it)) },
      label = { Text(stringResource(R.string.todo_name_req)) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
      ),
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )
    OutlinedTextField(
      value = itemDetails.description,
      onValueChange = { onValueChange(itemDetails.copy(description = it)) },
      label = { Text(stringResource(R.string.todo_desc_req)) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
      ),
      modifier = Modifier.fillMaxWidth(),
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
      Checkbox(checked = itemDetails.done, onCheckedChange = null)
      Text(
        text = stringResource(R.string.done),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(start = 16.dp)
      )
    }
    Text(
      text = stringResource(R.string.required_fields),
      modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ItemInputFormPreview() {
  ItemInputForm(itemDetails = ItemDetails())
}

@Composable
fun ItemEntryBody(
  itemUiState: ItemUiState,
  onItemValueChange: (ItemDetails) -> Unit,
  onSaveClick: () -> Unit,
  showDelete: Boolean = false,
  onDeleteClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(
      dimensionResource(id = R.dimen.padding_large)
    ),
    modifier = modifier.padding(
      dimensionResource(id = R.dimen.padding_medium)
    )
  ) {
    ItemInputForm(
      itemDetails = itemUiState.itemDetails,
      onValueChange = onItemValueChange,
      modifier = Modifier.fillMaxWidth()
    )
    Button(
      onClick = onSaveClick,
      enabled = itemUiState.isEntryValid,
      shape = MaterialTheme.shapes.small,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = stringResource(R.string.save_action))
    }
    if (showDelete) {
      Button(
        onClick = onDeleteClick,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
      ) {
        Text(text = stringResource(R.string.delete))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ItemEntryBodyPreview() {
  ItemEntryBody(
    itemUiState = ItemUiState(),
    onItemValueChange = {},
    onSaveClick = {}
  )
}