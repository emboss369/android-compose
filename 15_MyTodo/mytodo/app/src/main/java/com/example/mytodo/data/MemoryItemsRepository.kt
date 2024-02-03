package com.example.mytodo.data

class MemoryItemsRepository : ItemsRepository {
  val list = mutableListOf(
    Item(1, "Game", "詳細", true),
    Item(2, "Pen", "詳細", false),
    Item(3, "TV", "詳細", false)
  )

  override fun getAllItemsStream(): List<Item> = list
  override fun getItemStream(id: Int): Item =
    list.filter { it.id == id }.first()

  override suspend fun insertItem(item: Item) {
    list.add(item)
  }

  override suspend fun deleteItem(item: Item) {
    list.remove(item)
  }

  override suspend fun updateItem(item: Item) {
    list.remove(list.filter { it.id == item.id }.first())
    list.add(item)
  }
}