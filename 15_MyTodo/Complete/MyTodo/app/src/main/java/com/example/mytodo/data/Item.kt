package com.example.mytodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.ThreadLocalRandom
@Entity(tableName = "items")
data class Item(
  // id プロパティに @PrimaryKey アノテーションを付けて、id を主キーにします。主キーは、Item テーブルのすべてのレコードやエントリを一意に識別する ID です。
  // id にデフォルト値として 0 を割り当てます。これは、id が id 値を自動生成するために必要です。
  // Room が各エンティティの増分 ID を生成するように、パラメータ autoGenerate を true に設定します。このようにすることで、各アイテムの ID が一意になります。
@PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  //val id: Int = ThreadLocalRandom.current().nextInt(),
  val title: String,
  val description: String,
  val done: Boolean
)