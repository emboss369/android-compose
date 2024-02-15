package com.example.mytodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// exportSchemaは、Room Databaseのアノテーションパラメータの一つで、
// データベースのスキーマをエクスポートするかどうかを指定します。
// スキーマとは、データベースの構造（テーブル、カラム、インデックスなど）を指します。
// exportSchemaがtrueに設定されている場合、
// RoomはデータベースのスキーマをJSON形式でエクスポートします。
// これは、データベースのバージョン管理に役立ちます。デフォルトでは、この値はtrueです。
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
  abstract fun itemDao(): ItemDao

  companion object {
    // データベースの作成時に、データベースに対する参照を保持します。
    // @Volatileアノテーションは、このフィールドが他のスレッドによって変更される可能性があることを示します。
    // Instance の値が常に最新になり、すべての実行スレッドで同じになります。
    // つまり、あるスレッドが Instance に加えた変更が、
    // すぐに他のすべてのスレッドに反映されます。
    @Volatile
    private var Instance: TodoDatabase? = null

    fun getDatabase(context: Context): TodoDatabase {
      return Instance ?: synchronized(this) {
        Room.databaseBuilder(
          context, TodoDatabase::class.java, "item_database"
        ).build().also { Instance = it }
      }
    }
  }
}