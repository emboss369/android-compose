package com.example.mytodo.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodo.R
import com.example.mytodo.data.Item
import com.example.mytodo.ui.AppViewModelProvider
import com.example.mytodo.ui.TodoTopAppBar
import com.example.mytodo.ui.navigation.NavigationDestination
import com.example.mytodo.ui.theme.MyTodoTheme

object HomeDestination : NavigationDestination {
  override val route = "home"
  override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
  navigateToItemEntry: () -> Unit, // FABボタンタップ時の処理
  navigateToItemUpdate: (Int) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
  // import androidx.compose.runtime.getValue
  val homeUiState by viewModel.homeUiState.collectAsState()

  // TopAppBarScrollBehaviorを返します。このTopAppBarScrollBehaviorで設定されたトップアプリバーは、コンテンツがプルアップされると即座に折りたたまれ、コンテンツがプルダウンされると即座に表示されます。
  // 一部の M3 API は試験運用版とみなされています。その場合は、ExperimentalMaterial3Api アノテーションを使用して、関数レベルまたはファイルレベルでオプトインする必要があります。
  // または、moduleのbuild.gradleのkotlinOptionsに
  //　freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
  // を適用してモジュール全体に適用することも出来ます。
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Scaffold(
    // nestedScroll:要素を修正し、ネストされたスクロール階層に参加させる。
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TodoTopAppBar(
        title = stringResource(R.string.app_name/* TODO:FIX */),
        canNavigateBack = false, // トップ画面なので戻るアイコンは不要です
        scrollBehavior = scrollBehavior
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        // FABボタンタップ時の処理
        onClick = navigateToItemEntry,
        // このFABのコンテナとシャドウの形状を定義する(エレベーションを使用する場合)
        shape = MaterialTheme.shapes.medium,
        // dimensファイルを新規作成し、padding_largeを定義します。
        // このようにサイズに関する定義もXMLファイルで管理すると良い。
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
      ){
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = stringResource(R.string.item_entry_title)
        )
      }
    }
  ) { innerPadding ->
    HomeBody(
      itemList = homeUiState.itemList,
      onItemClick = navigateToItemUpdate,
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()

    )
  }
}

@Composable
private fun HomeBody(
  itemList: List<Item>,
  onItemClick: (Int) -> Unit,
  modifier: Modifier = Modifier
){
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    if(itemList.isEmpty()) {
      Text(
        text = stringResource(R.string.no_item_description),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
      )
    } else {
      TodoList(
        itemList = itemList,
        onItemClick = { onItemClick(it.id) },
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
      )
    }
  }
}

@Composable
private fun TodoList(
  itemList: List<Item>,
  onItemClick: (Item) -> Unit,
  modifier: Modifier = Modifier
){
  LazyColumn(modifier = modifier) {
    // itemsはインポート必要
    // import import androidx.compose.foundation.lazy.items
    // Adds a list of items.
    items(items = itemList, key = { it.id }) {item ->
      TodoItem(item = item,
        modifier = Modifier
          .padding(dimensionResource(id = R.dimen.padding_small))
          .clickable { onItemClick(item) })
    }
  }
}

@Composable
private fun TodoItem(
  item: Item, modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
      verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ){
      Row(
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.weight(1f))
        Text(
          text = if (item.done) stringResource(id = R.string.done)
          else stringResource(id = R.string.not_yet),
          style = MaterialTheme.typography.titleMedium
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
  MyTodoTheme {
    HomeBody(listOf(
      Item(1, "Game", "詳細",true), Item(2, "Pen", "詳細",false), Item(3, "TV", "詳細",false)
    ), onItemClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
  MyTodoTheme {
    HomeBody(listOf(), onItemClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview() {
  MyTodoTheme {
    TodoItem(
      Item(1, "Game", "詳細",false),
    )
  }
}