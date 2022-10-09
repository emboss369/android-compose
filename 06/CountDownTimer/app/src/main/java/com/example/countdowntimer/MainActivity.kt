package com.example.countdowntimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.countdowntimer.ui.theme.CountDownTimerTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountDownTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // https://engawapg.net/jetpack-compose/1426/work-with-viewmodel/
                    // も参考になりそうだ。

                    // 今回は、
                    // まずは、 implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.3.1"
                    // を追加した。
                    // https://developer.android.com/jetpack/compose/state?hl=ja#viewmodels-source-of-truth
                    // を参考にする。

                    // Greeting("Android")
                    ScaffoldDemo()
                }
            }
        }
    }
}

// https://developer.android.com/jetpack/compose/layout?hl=ja#slot-based-layouts
// Scaffold を使用すると、基本的なマテリアル デザインのレイアウト構造で UI を実装できます。
// Scaffold には、TopAppBar、BottomAppBar、FloatingActionButton、Drawer など、
// 最も一般的なトップレベルのマテリアル コンポーネント向けのスロットが用意されています。
// Scaffold を使用すると、こうしたコンポーネントを適切に配置し、
// ともに正しく動作させることが簡単になります。

// https://developer.android.com/jetpack/compose/layouts/material?hl=ja#scaffold
// を見ながら記事を書く。

// このサイトもサンプルが豊富で良い。
// https://foso.github.io/Jetpack-Compose-Playground/material/scaffold/
// Composeのサンプルがたくさんある。

// 日本語で Scaffoldのサンプルがあるブログ。
// https://www.gesource.jp/weblog/?p=8591

@Composable
fun ScaffoldDemo(viewModel: ExampleViewModel = viewModel()) {
    // androidx.lifecycle.viewmodel.composeパッケージのviewModel()
    // が使えます。インポート必要。
    //
    // 初めて呼び出したときは新しくインスタンスを作成し、すでにインスタンス作成済みの場合は
    // 同じインスタンスを返します。これについてはこの記事の最後で補足します。

    val materialBlue700= Color(0xFF1976D2)
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    val uiState = viewModel.uiState

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(background = materialBlue700,
            iconOnClick = {
                // https://developer.android.com/jetpack/compose/layouts/material
                // に、ドロアーをクリックしたときの処理について記載があります。
                scope.launch {
                    scaffoldState.drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        )  },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { FloatingActionButton(onClick = {
            if (uiState.isRunning) {
                viewModel.stopTimer()
            } else {
                viewModel.startTimer(1 * 60 * 1000)
            }
        }){
            if (uiState.isRunning) {
                Icon(Icons.Filled.Stop, contentDescription = "停止")
            } else {
                Icon(Icons.Filled.Timer, contentDescription = "開始")
            }
        } },
        drawerContent = { Text(text = "drawerContent") },
        content = {
            Box(modifier = Modifier.padding(
                start = 30.dp,
                end = 30.dp,
                top = 30.dp,
                bottom = 120.dp
            ),
                contentAlignment = Alignment.Center
            ){
                Arc(color = Color.Green,
                    timeLeft = uiState.timeLeft
                    )
                Text("%1d:%2$02d".format(uiState.minute,
                    uiState.second))
        }
                  },
        bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
    )
}
@Composable
fun Arc(color: Color, timeLeft: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/drawscope/DrawScope#drawArc(androidx.compose.ui.graphics.Brush,kotlin.Float,kotlin.Float,kotlin.Boolean,androidx.compose.ui.geometry.Offset,androidx.compose.ui.geometry.Size,kotlin.Float,androidx.compose.ui.graphics.drawscope.DrawStyle,androidx.compose.ui.graphics.ColorFilter,androidx.compose.ui.graphics.BlendMode)
        val strokeWidht = 30.0f
        val minSize = min(size.width, size.height) - strokeWidht * 4
        val maxSize = max(size.width, size.height)
        var offset: Offset




        if (size.width < size.height) {
            offset = Offset(strokeWidht * 2, (maxSize - minSize) / 2.0f )
        } else {
            offset = Offset((maxSize - minSize) / 2.0f, 0.0f )
        }

        drawArc(
            Color.Green,
            0f - 90f,
            360 * timeLeft,
            useCenter = false,
            size = Size(minSize, minSize),
            topLeft = offset,
            style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun TopBar(background: Color,
           iconOnClick: () -> Unit
) {
    // TopAppBarコンポーザブルを使う
    // https://techbooster.org/android/resource/18628/
    TopAppBar(
        title = {
            // 画像を使ってもOK!
            Text("TopAppBar")
                },
        navigationIcon = {
            IconButton(onClick = iconOnClick) {
                Icon(
                    // Iconはここから選んじゃおう
                    // https://developer.android.com/reference/kotlin/androidx/compose/material/icons/Icons
                    // https://material.io/design/iconography/system-icons.html#design-principles
                    // すべてのiconは Google Fontsのサイトで紹介されています。ワードで検索もできます。
                    // https://fonts.google.com/icons
                    // すべてのアイコンを使えるようにする
                    // Gradle Scriptsの　build.gradle(Module: <プロジェクト名>)
                    // を開き、一行追加する。
                    // implementation "androidx.compose.material:material-icons-extended:$compose_ui_version"
                    // 使うときはImport忘れずに。

                    imageVector = Icons.Filled.Timer,
                    contentDescription = stringResource(R.string.app_name),
                    tint = MaterialTheme.colors.background
                )
            }
        },
        backgroundColor = background
    )
}

@Composable
fun ScaffoldDemo1() {
    val materialBlue700= Color(0xFF1976D2)
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = {Text("TopAppBar")},backgroundColor = materialBlue700)  },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {}){
            Text("X")
        } },
        drawerContent = { Text(text = "drawerContent") },
        content = {

            Text("BodyContent") },
        bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
    )
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CountDownTimerTheme {
        //Greeting("Android")
        ScaffoldDemo()
    }
}