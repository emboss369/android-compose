package com.example.mytodo.data

import kotlinx.coroutines.flow.Flow

// 指定されたデータソースから[Item]の挿入、更新、削除、取得を提供するリポジトリ。
interface ItemsRepository {
  // 指定されたデータ・ソースからすべての項目を取得します。
  fun getAllItemsStream(): Flow<List<Item>>

  // 指定されたデータ・ソースから [id] に一致する項目を取得します。
  fun getItemStream(id: Int): Flow<Item?>

  // データ・ソースに項目を挿入する
  suspend fun insertItem(item: Item)

  // データ・ソースから項目を削除する
  suspend fun deleteItem(item: Item)

  // データソースの項目を更新する
  suspend fun updateItem(item: Item)


}