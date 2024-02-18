package com.example.mytodo.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mytodo.R
import com.example.mytodo.ui.navigation.TodoNavHost


//　アプリケーションの画面を表す最上位のコンポーザブル。
@Composable
fun TodoApp(
  navController: NavHostController = rememberNavController()
) {
  // NavController は Navigation コンポーネントの中心的な API です。
  // この API はステートフルであり、アプリ内の画面を構成するコンポーザブルのバックスタックと各画面の状態を追跡します。
  //NavController を作成するには、コンポーザブルで rememberNavController() メソッドを使用します。
  TodoNavHost(navController = navController)
}

//タイトルを表示し、条件付きでバックナビゲーションを表示するアプリバー。
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopAppBar(
  title: String,
  canNavigateBack: Boolean,
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
  navigateUp: () -> Unit = {}
) {
  //マテリアルデザインの中央に配置された小さなトップアプリバー。
  //トップアプリバーは、画面の上部に情報やアクションを表示します。
  //この小さなトップアプリバーは、ヘッダーのタイトルが水平に中央に配置されています。
  CenterAlignedTopAppBar(
    // Text は import androidx.compose.material3.Textをインポート
    title = { Text(title) }, // トップアプリバーに表示されるタイトル
    modifier = modifier,
    // TopAppBarScrollBehaviorは、トップアプリバーの高さと色を設定するために適用されるさまざまなオフセット値を保持します。スクロールビヘイビアは、スクロールされたコンテンツと連動して、コンテンツのスクロールに合わせてトップアプリバーの外観を変更するように設計されています。
    scrollBehavior = scrollBehavior,
    // トップ・アプリ・バーの最初に表示されるナビゲーション・アイコン。これは通常、IconButtonまたはIconToggleButtonでなければなりません。
    navigationIcon = {
      if (canNavigateBack) { // 前画面があれば、アイコン表示
        IconButton(onClick = navigateUp) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }

      }
    }
  )
}