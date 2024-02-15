package com.example.mytodo.data

class MemoryItemsRepository : ItemsRepository {
  val list = mutableListOf(
    Item(1, "Game", "詳細", true),
    Item(2, "Pen", "詳細", false),
    Item(3, "TV", "詳細", false),
    Item(4, "PC", "詳細", true),
    Item(5, "Book", "詳細", false),
    Item(6, "Phone", "詳細", true),
    Item(7, "Table", "詳細", false),
    Item(8, "Chair", "詳細", true),
    Item(9, "Cup", "詳細", false),
    Item(10, "Glass", "詳細", true),
    Item(11, "Bottle", "詳細", false),
    Item(12, "Bag", "詳細", true),
    Item(13, "Shoes", "詳細", false),
    Item(14, "Socks", "詳細", true),
    Item(15, "Hat", "詳細", false),
    Item(16, "Gloves", "詳細", true),
    Item(17, "Scarf", "詳細", false),
    Item(18, "Jacket", "詳細", true),
    Item(19, "Pants", "詳細", false),
    Item(20, "Shirt", "詳細", true)


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