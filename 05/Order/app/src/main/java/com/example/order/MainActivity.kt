package com.example.order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.ui.theme.OrderTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                    // HelloContent()
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    // https://developer.android.com/jetpack/compose/tutorial?hl=ja#animate-messages-while-expanding
    //var isOrderView by rememberSaveable { mutableStateOf(false) }

    // Bool型を保存する。rememberを使う。mutableStateOfでくくる
    // remeberでは、Bundleを使う。BundleはAndroidでデータを保持する仕組み。
    // Bundleは、基本的な型のほかに、Parcelableインターフェースを実装したクラスのインスタンスも受け付けてくれます。
    var isOrderView by remember { mutableStateOf(false) }

    if (isOrderView) {
        OrderView()
    } else {
        TopView(onTapButton = {
            isOrderView = true
        })
    }
}
// まずはTopViewづくりから
@Composable
fun TopView(onTapButton: () -> Unit) {
    // https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary?hl=ja#Image(androidx.compose.ui.graphics.painter.Painter,kotlin.String,androidx.compose.ui.Modifier,androidx.compose.ui.Alignment,androidx.compose.ui.layout.ContentScale,kotlin.Float,androidx.compose.ui.graphics.ColorFilter)

    Column(
        modifier = Modifier.background(Color.Yellow),
        // こっちはなぜかアライメント（整列）
        horizontalAlignment = Alignment.CenterHorizontally, // 横方向
        // こっちはなぜかアレンジメント（配置）紛らわしい！！！
        //verticalArrangement = Arrangement.Center // 縦方向
        verticalArrangement = Arrangement.SpaceEvenly // 空白を均等に
    ) {



        // https://developer.android.com/jetpack/compose/text?hl=ja
        Text(
            // text = "Colbar's burger",
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("Colbar's")
                }
                append(" burger ")
            },
            // Modifierそれぞれのコンポーザブルを修飾する、もしくは拡張するときに利用する。
            modifier = Modifier
                .background(color = Color.Black)
                .width(300.dp),
            // テキストの色
            color = Color.Cyan,
            // 文字サイズを変更する
            fontSize = 80.sp,
            // テキストを斜体にする
            fontStyle = FontStyle.Italic,
            // テキストを太字にする
            fontWeight = FontWeight.Bold,
            // テキストの配置
            textAlign = TextAlign.Center,
            // テキストのスタタイル
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White,
                    offset = Offset(5.0f, 5.0f),
                    blurRadius = 5.0f
                )
            ),
            // フォントの変更（Serif、Sans Serif、Monospace、Cursive ）
            fontFamily = FontFamily.Cursive
        )

        Image(painter = painterResource(id = R.drawable.hamburger), contentDescription = "hamburger")


        /// TODO: TextとImageをBoxで重ね浅せることで素敵なタイトルを完成させたい。
        /// TODO: ハンバーガーの画像を画面の横幅いっぱいに表示する。
        /// TODO: ハンバーガーの画像は、広告で使われるような雰囲気の加工もいれてもいいかも。SALEのスタンプや

        Button(onClick = {
            // 状態ホイスティング
            onTapButton()
        }) {
            Text("注文へ")
        }
    }
}


//@Composable
//fun OrderView() {
//
//    var isOrderView by rememberSaveable { mutableStateOf(false) }
//
//    if (isOrderView) {
//        OrderView()
//    } else {
//        TopView(onTapStart = {
//            isOrderView = true
//        })
//    }
//}

@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by remember { mutableStateOf("") }
        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}

@Composable
fun OrderView() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = "注文画面")

        var hamburger by remember { mutableStateOf("ハンバーガー") }

        // セットメニューを注文する画面を作りましょう。

        Text(text = "ハンバーガーをお選びください")
        // ラジオボタンで３択から選択。ハンバーガー、チーズバーガー
        Row(Modifier.selectableGroup()) {
            RadioButton(selected = hamburger == "ハンバーガー", onClick = {
                hamburger = "ハンバーガー" // 通常は配列を利用したり、定数を使ったりと効率化しますが、説明のためわかりやすく書いています。
            })

            RadioButton(selected = hamburger == "チーズバーガー", onClick = {
                hamburger = "チーズバーガー" // 通常は配列を利用したり、定数を使ったりと効率化しますが、説明のためわかりやすく書いています。
            })
        }

        // ラジオボタンとテキストを組み合わせるには、次のようにします。
        Column(Modifier.selectableGroup()) {
            Row(Modifier.selectable(
                selected = (hamburger == "ハンバーガー"),
                onClick = { hamburger = "ハンバーガー" },
                role = Role.RadioButton
            )) {
                RadioButton(selected = hamburger == "ハンバーガー", onClick = null)
                Text(text = "ハンバーガー")
            }
            Row(Modifier.selectable(
                selected = (hamburger == "チーズバーガー"),
                onClick = { hamburger = "チーズバーガー" },
                role = Role.RadioButton
            )) {
                RadioButton(selected = hamburger == "チーズバーガー", onClick = null)
                Text(text = "チーズバーガー")
            }
        }

        // 各項目はDividerで区切ろう
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#Divider(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp)
        Divider(modifier = Modifier.padding(24.dp))


        Text(text = "サイドメニューをお選びください")

        // サイドメニューはチェックボックスで。
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#Checkbox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.material.CheckboxColors)

        // フレンチフライfrench fry
        // オニオンリングonionRings
        // チキンナゲットchickenNugget
        var frenchFry by remember { mutableStateOf(false) }

        Checkbox(
            checked = frenchFry,
            onCheckedChange = { frenchFry = it }
        )

        // Checkboxもラベルを追加して、Rowがクリックされたら変更するように変更。詳しい説明はここ
        // https://developer.android.com/jetpack/compose/accessibility?hl=ja
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
        // 冗長なのでこれはカット、シンプルに作ろうね
//        var onionRings by remember { mutableStateOf(false) }
//        Row(Modifier.toggleable(
//            value = onionRings,
//            role = Role.Checkbox,
//            onValueChange = { onionRings = !onionRings})
//        ) {
//            Checkbox(
//                checked = onionRings,
//                onCheckedChange = null
//            )
//            Text(text = "オニオンリング")
//        }

        Divider(modifier = Modifier.padding(24.dp))
        Text(text = "ソースの量を調整できます")

        // ソース少なめ <-------> 多め、をSlidarで作る
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#Slider(kotlin.Float,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.ranges.ClosedFloatingPointRange,kotlin.Int,kotlin.Function0,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.material.SliderColors)
        var sliderPosition by remember { mutableStateOf(0f) }
        // 初期値では0.0から1.0までが選択範囲
        Text(text = sliderPosition.toString())
        // わかりやすく少なめ、多め、普通と表示するようにしてみましょう。
        Text(text = if (sliderPosition < 0.3) {"少なめ"} else if (sliderPosition > 0.7) {"多め"} else {"普通"} )
        Slider(value = sliderPosition, onValueChange = { sliderPosition = it })

        // スライダーには他にも
        //        valueRange = 0f..100f,
        //        onValueChangeFinished = {
        //            // launch some business logic update with the state you hold
        //            // viewModel.updateSelectedSliderValue(sliderPosition)
        //        },
        //        steps = 5,
        // といった引数が使えます。

        // ドリンクはDropDownMenuにしよう
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary?hl=ja#DropdownMenu(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.unit.DpOffset,androidx.compose.ui.window.PopupProperties,kotlin.Function1)
        // コーラ、アイスコーヒー、アイスカフェオレ、ジンジャエール、オレンジジュース、アップルジュース、メロンソーダ、ウーロン茶、ブレンドコーヒー、紅茶
        Divider(modifier = Modifier.padding(24.dp))
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
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .clickable { expanded = true }
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(20.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Text(text = drink)
                //Spacer(Modifier.fillMaxWidth())
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Localized description")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { drink = "アイスコーヒー" }) {
                    Text(text = "アイスコーヒー")
                }
                DropdownMenuItem(onClick = { drink = "アイスカフェオレ" }) {
                    Text(text = "アイスカフェオレ")
                }
                Divider()
                DropdownMenuItem(onClick = { drink = "コーラ" }) {
                    Text(text = "コーラ")
                }
            }

//            Button(onClick = {
//                // 状態ホイスティング
//                onTapOrder()
//            }) {
//                Text("注文へ")
//            }

        }

    }
}

// 注文内容を元のビューに伝えるためのデータクラスを作る。ここにデータを入れて、Viewの呼び出し元に注文内容を伝えます。

data class Order(val hamburger: String, val frenchFry: String,val sliderPosition: Float, val drink: String)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OrderTheme {
        //MainScreen()
        //HelloContent()
        OrderView()
    }
}