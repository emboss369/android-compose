package com.example.mytodo.ui.navigation

interface NavigationDestination {
  /**
   * コンポーザブルなパスを定義するための一意な名前。
   */
  val route: String

  /**
   * 画面に表示するタイトルを含む文字列リソースID。
   */
  val titleRes: Int
}