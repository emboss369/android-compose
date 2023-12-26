package com.example.order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      OrderTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          OrderView()
        }
      }
    }
  }
}

@Composable
fun OrderView() {
  Column(modifier = Modifier.padding(24.dp)) {
    Text(text = "注文画面")
    var hamburger by remember { mutableStateOf("ハンバーガー") }
    // ラジオボタンとテキストを組み合わせるには、次のようにします。
    Column(
      Modifier.selectableGroup()
    ) {
      Row(
        Modifier
          .selectable(
            selected = (hamburger == "ハンバーガー"),
            onClick = { hamburger = "ハンバーガー" },
            role = Role.RadioButton
          )
      ) {
        RadioButton(selected = hamburger == "ハンバーガー", onClick = null)
        Text(text = "ハンバーガー")
      }
      Row(
        Modifier
          .selectable(
            selected = (hamburger == "チーズバーガー"),
            onClick = { hamburger = "チーズバーガー" },
            role = Role.RadioButton
          )
      ) {
        RadioButton(selected = hamburger == "チーズバーガー", onClick = null)
        Text(text = "チーズバーガー")
      }
    }

    // 各項目はDividerで区切ろう
    // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#Divider(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp)
    Divider()
    Text(text = "サイドメニューをお選びください")

    var frenchFry by remember { mutableStateOf(false) }

    Row(Modifier.toggleable(
      value = frenchFry,
      role = Role.Checkbox,
      onValueChange = { frenchFry = !frenchFry})
    ) {
      Checkbox(
        checked = frenchFry,
        onCheckedChange = null
      )
      Text(text = "フレンチフライ")
    }
    Divider()
    Text(text = "ソースの量を調整できます")


    // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#Slider(kotlin.Float,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.ranges.ClosedFloatingPointRange,kotlin.Int,kotlin.Function0,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.material.SliderColors)
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toString())
    Text(
      text =
      if (sliderPosition < 0.3) {
        "少なめ"
      } else if (sliderPosition > 0.7) {
        "多め"
      } else {
        "普通"
      }
    )
    Slider(
      value = sliderPosition,
      onValueChange = { sliderPosition = it }
    )

    Divider()
    Text(text = "セットドリンクをお選びください")
    var expanded by remember { mutableStateOf(false) }
    var drink by remember { mutableStateOf("") }

    // ドロップダウンメニューは、Boxと一緒に使います。

    Box(
      modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)
    ) {
      // ROWを使い、選択したドリンク名をテキストで表示、ドロップダウンメニューでよくある表現として下向きの矢印アイコンを配置したいと思います。
      // 領域いっぱいに広げて、ドリンク名テキストを左寄せ、下向きの矢印アイコンを右寄せします。horizontalArrangementに、SpaceBetweenを使いましょう。
      // Rowをクリックしたらexpandedが切り替わるようにします。モディファイアのclickableを使います。
      Row(modifier = Modifier
        .width(200.dp)
        .clickable { expanded = true }
        .border(
          width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(4.dp)
        ), horizontalArrangement = Arrangement.SpaceBetween)
      {
        Text(text = drink)
        //Spacer(Modifier.fillMaxWidth())
        Icon(
          Icons.Default.ArrowDropDown,
          contentDescription = "ArrowDropDown"
        )
      }

      DropdownMenu(expanded = expanded,
        onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
          text = { Text(text = "アイスコーヒー") },
          onClick = { drink = "アイスコーヒー" }
        )

        DropdownMenuItem(
          text = { Text(text = "アイスカフェオレ") },
          onClick = { drink = "アイスカフェオレ" }
        )
        Divider()
        DropdownMenuItem(
          text = { Text(text = "コーラ") },
          onClick = { drink = "コーラ" }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun OrderViewPreview() {
  OrderTheme {
    OrderView()
  }
}