package com.example.mytodo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.flow.MutableStateFlow

object HomeDestination : NavigationDestination {
  override val route = "home"
  override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  navigateToItemEntry: () -> Unit = {},
  navigateToItemUpdate: (Int) -> Unit = {},
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

  val itemList by viewModel.homeUiState.itemList.collectAsState(initial = emptyList())
  var showDone by remember { mutableStateOf(false) }
  var filteredItemList by remember(itemList, showDone) {
    mutableStateOf(itemList.filter {
      if (showDone) true
      else it.done == false
    })
  }

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TodoTopAppBar(
        title = stringResource(R.string.app_name),
        canNavigateBack = false,
        scrollBehavior = scrollBehavior
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = navigateToItemEntry,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
          .padding(dimensionResource(id = R.dimen.padding_large))
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = stringResource(R.string.item_entry_title)
        )
      }
    }) { innerPadding ->

    HomeBody(
      itemList = filteredItemList,
      onItemClick = { navigateToItemUpdate(it.id) },
      onCheckedChange = {
        showDone = it
      },
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
    )
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  val testRepo = object : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = MutableStateFlow(
      listOf(
        Item(1, "Item 1", "Description 1", false),
        Item(2, "Item 2", "Description 2", true)
      )
    )

    override fun getItemStream(id: Int): Flow<Item?> = MutableStateFlow(
      Item(1, "Item 1", "Description 1", false)
    )

    override suspend fun insertItem(item: Item) {}
    override suspend fun deleteItem(item: Item) {}
    override suspend fun updateItem(item: Item) {}
  }
  HomeScreen(
    navigateToItemEntry = {},
    navigateToItemUpdate = {},
    viewModel = HomeViewModel(testRepo)
  )
}

@Composable
private fun TodoItem(
  item: Item, modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier
          .padding(dimensionResource(id = R.dimen.padding_large)),
        verticalArrangement = Arrangement
          .spacedBy(dimensionResource(id = R.dimen.padding_small))
      ) {
        Row(

          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = item.title, style = MaterialTheme.typography.titleLarge
          )
          Spacer(Modifier.weight(1f))
          Text(
            text = if (item.done) stringResource(R.string.done)
            else stringResource(R.string.not_yet),
            style = MaterialTheme.typography.titleMedium
          )
        }
      }
      if (item.done) {
        Divider(
          modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium)),
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview() {
  TodoItem(item = Item(1, "タイトル", "詳細", true))
}

@Composable
private fun HomeBody(
  itemList: List<Item>,
  onItemClick: (Item) -> Unit = {},
  onCheckedChange: (Boolean) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var checked by remember { mutableStateOf(false) }
  LazyColumn(modifier = modifier) {
    item {
      Row(
        Modifier.toggleable(value = checked,
          role = Role.Checkbox,
          onValueChange = {
            checked = it
            onCheckedChange(it)
          })
      ) {
        Checkbox(
          checked = checked, onCheckedChange = null
        )
        Text(text = stringResource(R.string.show_completed_todo))
      }
    }
    if (itemList.isEmpty()) {
      item {
        Text(
          text = stringResource(R.string.no_item_description),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleLarge
        )
      }
    }
    items(items = itemList, key = { it.id }) { item ->
      TodoItem(item = item,
        modifier = Modifier
          .padding(dimensionResource(id = R.dimen.padding_small))
          .clickable { onItemClick(item) })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
  HomeBody(
    itemList = listOf(
      Item(1, "タイトル", "詳細", true),
      Item(2, "タイトル", "詳細", false)
    )
  )
}