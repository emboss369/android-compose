package com.example.mytodo.data

import android.content.Context

// 依存性注入のためのアプリコンテナ。
interface AppContainer {
  val itemsRepository: ItemsRepository
}

// [OfflineItemsRepository]のインスタンスを提供する[AppContainer]の実装。
class AppDataContainer(private val context: Context) : AppContainer {
  /**
   * Implementation for [ItemsRepository]
   */
  override val itemsRepository: ItemsRepository by lazy {
    OfflineItemsRepository()
  }
}

// [OfflineItemsRepository]のインスタンスを提供する[AppContainer]の実装。
class AppDataMemoryContainer(private val context: Context) : AppContainer {
  /**
   * Implementation for [ItemsRepository]
   */
  override val itemsRepository: ItemsRepository by lazy {

    MemoryItemsRepository() // テスト用。メモリ上にに保存する。
  }
}