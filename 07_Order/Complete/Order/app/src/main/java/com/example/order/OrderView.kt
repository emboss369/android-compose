package com.example.order

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.order.ui.theme.OrderTheme

@Composable
fun OrderView() {
  Column(modifier = Modifier.padding(24.dp)) {
    Text(text = "注文画面")
    var hamburger by remember{ mutableStateOf("ハンバーガー") }
    Column(modifier = Modifier.selectableGroup()) {
      Row(
        modifier = Modifier
          .selectable(
            selected = hamburger == "ハンバーガー",
            onClick = { hamburger = "ハンバーガー" },
            role = Role.RadioButton
          )

      ){
        RadioButton(
          selected = hamburger == "ハンバーガー",
          onClick = null
        )
        Text(text = "ハンバーガー")
      }
      Row(
        modifier = Modifier
          .selectable(
            selected = hamburger == "チーズバーガー",
            onClick = { hamburger = "チーズバーガー" },
            role = Role.RadioButton
          )
      ){
        RadioButton(
          selected = hamburger == "チーズバーガー",
          onClick = null
        )
        Text(text = "チーズバーガー")
      }
    }

    Divider()
    Text(text = "サイドメニューを選択してください")
    var frenchFry by remember{ mutableStateOf(false) }
    Row(
      modifier = Modifier
        .toggleable(
          value = frenchFry,
          onValueChange = { frenchFry = !frenchFry },
          role = Role.Checkbox
        )
    ) {
      Checkbox(
        checked = frenchFry,
        onCheckedChange = null
      )
      Text(text = "フレンチフライ")
    }
    // 区切り線を配置
    // Textで"ソースの量を調整できます"と表示
    // sliderPositionというremember変数を0fで初期化
    // TextでsliderPositionの値を表示
    // TextでsliderPositionが0.3より少なければ"少なめ"と表示し、 sliderPositionが0．7より多ければ多めと表示、それ以外は普通と表示
    // Slider配置し変更したら位置をsliderPositionに格納する
    Divider()
    Text(text = "ソースの量を調整できます")
    var sliderPosition by remember{ mutableStateOf(0f) }
    Text(text = sliderPosition.toString())
    Text(text = when {
      sliderPosition < 0.3f -> "少なめ"
      sliderPosition > 0.7f -> "多め"
      else -> "普通"
    })
    Slider(
      value = sliderPosition,
      onValueChange = { sliderPosition = it }
    )
    // 区切り線を配置
    Divider()
    // Textで"ドリンクを選択してください"と表示
    Text(text = "ドリンクを選択してください")
    // expandedとdrinkと言う変数を用意し、それぞれフォルストから文字列で初期化します
    var expanded by remember{ mutableStateOf(false) }
    var drink by remember{ mutableStateOf("") }
    // Boxは画面全体を覆うコンテナで、その中にRowとDropdownMenuを配置します
    Box(
      modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)
    ) {
      Row(
        modifier = Modifier
          .width(200.dp)
          .clickable { expanded = true }
          .border(
            width = 1.dp,
            color = Color.DarkGray,
            shape = RoundedCornerShape(4.dp)
          ),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = drink)
        Icon(
          imageVector = Icons.Filled.ArrowDropDown,
          contentDescription = "dropdown"
        )
      }
      DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
      ) {
        // ドロップダウンメニューアイテムはドロップダウンメニュー内の各項目を表します。テキストパラメーターにアイスコーヒーと言うテキストをお持ちクリックされると、ドリンク変数にアイスコーヒーが代入され、エキスパンデッドがホルスに設定されるようにしましょう。
        DropdownMenuItem(
          text = { Text(text = "アイスコーヒー") },
          onClick = {
            drink = "アイスコーヒー"
            expanded = false
          }
        )
        // アイスカフェオレ
        DropdownMenuItem(
          text = { Text(text = "アイスカフェオレ") },
          onClick = {
            drink = "アイスカフェオレ"
            expanded = false
          }
        )
        Divider()
        // コーラ
        DropdownMenuItem(
          text = { Text(text = "コーラ") },
          onClick = {
            drink = "コーラ"
            expanded = false
          }
        )

      }
    }
  }
}

@Preview
@Composable
fun OrderViewPreview() {
  OrderTheme{
    OrderView()
  }
}
