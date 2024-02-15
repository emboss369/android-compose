package com.example.mytodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// @DaoはRoom が実装を提供するために必要なアノテーション
@Dao
interface ItemDao {
  @Insert
  suspend fun insert(item: Item)

  @Update
  suspend fun update(item: Item)

  @Delete
  suspend fun delete(item: Item)

  @Query("SELECT * from items WHERE id = :id")
  fun getItem(id: Int): Flow<Item>

  @Query("SELECT * from items ORDER BY id DESC")
  fun getAllItems(): Flow<List<Item>>
}