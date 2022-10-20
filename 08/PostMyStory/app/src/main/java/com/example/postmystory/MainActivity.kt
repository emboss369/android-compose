package com.example.postmystory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.postmystory.ui.theme.PostMyStoryTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    enum class Scene {
        LIST, PHOTOS, CAPTION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val messages = remember {
                mutableStateListOf(
                    Message(
                        "伊賀百貨店は、今月3月25日に大阪市内にオープンしたばかりの新しいお店です。".repeat(5),
                        "https://picsum.photos/seed/1/200",
                        11
                    ),
                    Message(
                        "お店は、とてもカラフルできれいで、なかなか良い感じです。".repeat(5),
                        "https://picsum.photos/seed/2/200",
                        250
                    ),
                    Message(
                        "服装を見るのはとても興味深いです。設備はとても充実しています。".repeat(5),
                        "https://picsum.photos/seed/3/200",
                        0
                    ),
                    Message(
                        "友達と遊ぶのにいい場所だと思ったし、店の前で働くのも楽しかった。".repeat(5),
                        "https://picsum.photos/seed/4/200",
                        462
                    )
                )
            }
            var scene by remember{mutableStateOf(Scene.LIST)}
            var selectUrl by remember{ mutableStateOf("") }
            var caption by remember{ mutableStateOf("") }

            PostMyStoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //PhotoGrid() {}
                    when(scene) {
                        Scene.LIST -> ListScreen(messages = messages){
                            scene = Scene.PHOTOS
                            selectUrl  = ""
                            caption = ""
                        }
                        Scene.PHOTOS -> PhotoGrid(){ url ->
                            scene = Scene.CAPTION
                            selectUrl = url
                        }
                        Scene.CAPTION -> CaptionView(
                            onClick = {
                                      messages.add(0,
                                          Message(
                                          caption = caption,
                                          image = selectUrl,
                                          nice = 0
                                      ))
                                scene = Scene.LIST
                        }, onChange = {newCaption ->
                            caption = newCaption
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CaptionView(onClick:() -> Unit, onChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Column() {
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                onChange(text) },
            label = { Text("キャプションを書く") }
        )
        Button(onClick = {
            onClick()
        }) {
Text(text = "記事を追加する")
        }
    }

}

@Composable
fun PhotoGrid(onClick: (String) -> Unit) {
    val photos = mutableListOf<String>().apply {
        for(i in 1..24) {
            add("https://picsum.photos/seed/${i}/200")
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {

        items(items = photos) { photo ->
            PhotoItem(photo, onClick)
        }
    }
}


@Composable
fun PhotoItem(photo: String, onClick: (String) -> Unit) {

    AsyncImage(
        // タップした画像のURLを返す。
        modifier = Modifier.clickable { onClick(photo) },
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo)
            .crossfade(true)
            // coliのAsyncImageは高機能、メモリキャッシュ機能・ディスクキャッシュ機能も標準装備。無効にしたいばあいはdiskCachePolicyでDISABLEDにすることもできます。
            //.memoryCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            //.transformations(RoundedCornersTransformation(40f))
            .build(),

        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Preview(showBackground = true)
@Composable
fun PhotoGridPreview() {
    PostMyStoryTheme {
        PhotoGrid(){}
    }
}



@Composable
fun Greeting(name: String) {

    LazyColumn {
        // Add a single item
        item {
            Text(text = "Hello $name!")
        }

        // Add 5 items
        items(100) { index ->
            Text(text = "Item: $index")
        }

        // Add another single item
        item {
            Text(text = "Last item")
        }
    }
}

@Stable
data class Message(
    val caption: String,
    val image: String,
    val nice: Int
)

//@Composable
//fun MessageRow(message: Message) {
//    Text(text = message.title)
//}

//@Composable
//fun MessageList(messages: List<Message>) {
//    LazyColumn {
//        // itemsを使うには
//        // import androidx.compose.foundation.lazy.items
//        // を明示的にインポートする必要がある。
//        items(messages) { message ->
//            MessageRow(message)
//        }
//    }
//}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeader() {
    val sections = listOf("A", "B", "C", "D", "E", "F", "G")

    LazyColumn(reverseLayout = false, contentPadding = PaddingValues(6.dp)) {
        sections.forEach { section ->
            // stickyHeader を使えば、セクションを使えます。
            // しかしこれは本稿執筆時点では実験的な機能で
            // 将来的に変更される可能背があります。
            // この実験的な機能を使用するためには、
            // @OptIn(ExperimentalFoundationApi::class)
            // を指定する必要があります。
            stickyHeader {
                Text(
                    "Section $section",
                    Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }
            items(10) {
                Text("Item $it from the section $section")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StickyHeaderPreview() {
    PostMyStoryTheme {
        //Greeting("Android")
        StickyHeader()
    }
}


@Composable
fun ListScreen(messages: MutableList<Message>, onClick: () -> Unit) {
    val state = rememberLazyListState()
    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    Box() {

        LazyColumn(state = state) {
            // itemsを使うには
            // import androidx.compose.foundation.lazy.items
            // を明示的にインポートする必要がある。
            items(messages) { message ->
                ArticleView(message = message)
            }
        }

        Box(
            // 親のコンポーザブルまたは画面の取りうる最大サイズに合わせる場合は、fillMaxSizeを使います。
            modifier = Modifier.fillMaxSize(),
            // 内部のコンポーネントに配置を指定するには、contentAlignmentにAlignmentを指定します。
            contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    onClick()
//                    messages.add(0,
//                        Message(
//                            "ニューヨークに行く前、20年近く同じ会社に勤めていたそうですね。",
//                            "https://picsum.photos/seed/5/200",
//                            0
//                        )
//                    )
//                    coroutineScope.launch {
//                        // Animate scroll to the 10th item
//                        state.animateScrollToItem(index = 0)
//                    }

                }) {
                Icon(Icons.Filled.Add, contentDescription = "追加")
            }
        }
    }
}

@Composable
fun ArticleList(messages: MutableList<Message>) {
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        // itemsを使うには
        // import androidx.compose.foundation.lazy.items
        // を明示的にインポートする必要がある。
        items(messages) { message ->
            ArticleView(message = message)
        }
    }
}



@Composable
fun ArticleView(message: Message) {
    Column(
        // Columnがスクロールするように設定するには次のように
        // モディファイアでverticalScrollを設定します。
        // https://developer.android.com/jetpack/compose/gestures?hl=ja#scroll-modifiers
//        modifier = Modifier
//            .verticalScroll(rememberScrollState())
    ) {
        // https://developer.android.com/jetpack/compose/libraries#image-loading
        // Instacart の Coil ライブラリには、ネットワーク経由でリモート画像を読み込むなど、外部ソースから画像を読み込むためのコンポーズ可能な関数が用意されています。
        // CoilはKotlin コルーチンに支えられた Android 用の画像読み込みライブラリ。
        // 高速・軽量・使いやすいライブラリです。
        // implementation("io.coil-kt:coil-compose:2.2.2")
        // で導入。
        // <uses-permission android:name="android.permission.INTERNET" />
        // 追加
        // https://coil-kt.github.io/coil/compose/#observing-asyncimagepainterstate
        // ここでは、picsum.photosというサービスを使っています。「Lorem Picsum（https://picsum.photos/）」はフォトサービス「Unsplash」の画像を利用したWEBサービスで、無料のダミー画像を使うことができます。
        // お好きな画像URLに変更して頂いても良いでしょう。

        AsyncImage(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(message.image)
                .crossfade(true)
                // coliのAsyncImageは高機能、メモリキャッシュ機能・ディスクキャッシュ機能も標準装備。無効にしたいばあいはdiskCachePolicyでDISABLEDにすることもできます。
                //.memoryCachePolicy(CachePolicy.DISABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .transformations(RoundedCornersTransformation(40f))
                .build(),

            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = null
            )

            Text(text = "「Nice！」${message.nice}件")
        }
        // おりたたみ
        var folding by remember { mutableStateOf(true) }
        // clickable 修飾子を使用すると、クリックの検出が容易になり、タップ時にユーザー補助機能を提供するとともに、視覚的インジケーター（リップルなど）を表示できます。
        Text(
            text = message.caption, maxLines = if (folding) 2 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(16.dp)
                .clickable { folding = !folding }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticlePreview() {
    val messages = mutableListOf(
        Message(
            "伊賀百貨店は、今月3月25日に大阪市内にオープンしたばかりの新しいお店です。".repeat(5),
            "https://picsum.photos/seed/1/200",
            11
        ),
        Message(
            "お店は、とてもカラフルできれいで、なかなか良い感じです。".repeat(5),
            "https://picsum.photos/seed/2/200",
            250
        ),
        Message(
            "服装を見るのはとても興味深いです。設備はとても充実しています。".repeat(5),
            "https://picsum.photos/seed/3/200",
            0
        ),
        Message(
            "友達と遊ぶのにいい場所だと思ったし、店の前で働くのも楽しかった。".repeat(5),
            "https://picsum.photos/seed/4/200",
            462
        )
    )
    PostMyStoryTheme {
        ListScreen(messages = messages){}
    }
}