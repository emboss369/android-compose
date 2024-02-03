package com.example.mytodo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mytodo.ui.home.HomeDestination
import com.example.mytodo.ui.home.HomeScreen
import com.example.mytodo.ui.item.ItemEditDestination
import com.example.mytodo.ui.item.ItemEditScreen
import com.example.mytodo.ui.item.ItemEntryDestination
import com.example.mytodo.ui.item.ItemEntryScreen


//  NavController は 1 つの NavHost コンポーザブルに関連付ける
// NavHost は NavController にナビゲーション グラフを関連付ける。
@Composable
fun TodoNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  // NavHost の作成では、Navigation Kotlin DSL のラムダ構文を使用して、
  // ナビゲーション グラフを作成

  /** [NavigationDestination] interfaceを作ったので各画面に実装しておく
   */
  // composable() メソッドを使用して、ナビゲーション構造に遷移先画面を追加できます。
  NavHost(
    navController = navController,
    startDestination = HomeDestination.route,
    modifier = modifier
  ) {

    // ホーム画面への遷移
    composable(route = HomeDestination.route) {
      HomeScreen(
        // FABボタンタップ時の処理
        navigateToItemEntry = {
          /**
           * ## コンポーザブルに移動する
           * ナビゲーション グラフ内のコンポーザブルのデスティネーションに移動するには、
           * navigate メソッドを使用する必要があります。navigate は、デスティネーションの
           * ルートを表す単一の String パラメータを取ります。ナビゲーション
           * グラフ内でコンポーザブルから移動するには、navigate を呼び出します。
           * https://developer.android.com/jetpack/compose/navigation?hl=ja
           */
          navController.navigate(ItemEntryDestination.route)
        },
        navigateToItemUpdate = {
          navController.navigate("${ItemEditDestination.route}/${it}")
        }
      )
    }

    // 新規追加画面への遷移
    composable(route = ItemEntryDestination.route) {
      ItemEntryScreen(
        navigateBack = {
          /**
           * ## ポップバック
           * NavController.popBackStack() メソッドでは、バックスタックから
           * 現在のデスティネーションがポップされ、前のデスティネーションへの
           * 移動が行われます。これにより、ユーザーは実質的にナビゲーション履歴内の
           * 1 つ前のステップに戻ることができます。
           * このメソッドでは、正常にポップされてデスティネーションに戻ったかどうかを
           * 示すブール値が返されます。
           * https://developer.android.com/guide/navigation/backstack?hl=ja
           */
          navController.popBackStack()
        },
        onNavigateUp = {
          /**
           * 最上部のデスティネーションをポップする: [上へ] または [戻る] をタップする
           * navigateUp()も、popBackStack()と同じく、1つ前のステップに戻ります。
           * 違いとしては、navigateUpの場合はディープリンクにも対応している点です。
           * ナビゲーションバーの左上にある「上へ」ボタンを押す場合に適しています。
           */
          navController.navigateUp()
        }
      )
    }

    composable(
      route = ItemEditDestination.routeWithArgs,
      // arguments パラメータは NamedNavArgument のリストを受け取ります。
      // navArgument メソッドを使用して NamedNavArgument をすばやく作成してから、その正確な type を指定できます。
      arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
        type = NavType.IntType
      })
    ) {
      ItemEditScreen(
        navigateBack = { navController.popBackStack() },
        onNavigateUp = { navController.navigateUp() }
      )
    }

  }
}