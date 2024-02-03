package com.example.mytodo.data

class OfflineItemsRepository : ItemsRepository {
  override fun getAllItemsStream(): List<Item> {
    TODO("Not yet implemented")
  }

  override fun getItemStream(id: Int): Item {
    TODO("Not yet implemented")
  }

  override suspend fun insertItem(item: Item) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteItem(item: Item) {
    TODO("Not yet implemented")
  }

  override suspend fun updateItem(item: Item) {
    TODO("Not yet implemented")
  }
}