package com.example.mytodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.ThreadLocalRandom

// エンティティ・データ・クラスは、データベース内の単一の行を表します。
// Item クラス宣言の上で、データクラスに @Entity アノテーションを付けます。tableName 引数を使用して、items を SQLite テーブル名として設定します。
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