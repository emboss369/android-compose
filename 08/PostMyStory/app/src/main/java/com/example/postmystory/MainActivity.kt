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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation

class MainActivity : ComponentActivity() {
    enum class Scene {
        LIST, PHOTOS, CAPTION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val messages = remember {
//                mutableStateListOf(
//                    Message(
//                        "この鉄橋は、昔の人々の技術と努力の結晶です。長い年月を経てなおしっかりと立っています。".repeat(5),
//                        "https://picsum.photos/seed/1/200",
//                        11
//                    )
//                    ,
//                    Message(
//                        "デジタルとアナログの両方を使って仕事をする人の気持ちを表現しています。".repeat(5),
//                        "https://picsum.photos/seed/2/200",
//                        250
//                    ),
//                    Message(
//                        "私は、この場所で心身ともに癒されました。".repeat(5),
//                        "https://picsum.photos/seed/3/200",
//                        0
//                    ),
//                    Message(
//                        "先日、友人と一緒にいちご狩りに行ってきました。".repeat(5),
//                        "https://picsum.photos/seed/4/200",
//                        462
//                    )
//                )
//            }
            val messages = remember { mutableStateListOf<Message>() }
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
                        Scene.PHOTOS -> PhotoGridScreen(){ url ->
                            scene = Scene.CAPTION
                            selectUrl = url
                        }
                        Scene.CAPTION -> CaptionScreen(
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
fun Greeting(name: String) {

    LazyColumn {
        // Add a single item
        item {
            Text(text = "Hello $name!")
        }

        // Add 5 items
        items(5) { index ->
            Text(text = "Item: $index")
        }

        // Add another single item
        item {
            Text(text = "Last item")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting(name = "Compose!")
}

// @Stable
// すべての入力が安定していて変化がなければ、再コンポジションをスキップ
// パフォーマンスに影響
@Stable // https://developer.android.com/jetpack/compose/lifecycle?hl=ja
data class Message(
    val caption: String,
    val image: String,
    val nice: Int
)

@Composable
fun MessageRow(message: Message) {
    Text(text = message.caption)
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn {
        // itemsを使うには
        // import androidx.compose.foundation.lazy.items
        // を明示的にインポートする必要がある。
        items(messages) { message ->
            MessageRow(message)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MessageListPreview() {
    val messages = mutableListOf(
        Message(
            "1".repeat(5),
            "https://picsum.photos/seed/1/200",
            11
        ),
        Message(
            "2".repeat(5),
            "https://picsum.photos/seed/2/200",
            250
        ),
        Message(
            "3".repeat(5),
            "https://picsum.photos/seed/3/200",
            0
        ),
        Message(
            "4".repeat(5),
            "https://picsum.photos/seed/4/200",
            462
        )
    )
    PostMyStoryTheme {
        //Greeting("Android")
        MessageList(messages = messages)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeader() {
    val sections = listOf("A", "B", "C", "D", "E", "F", "G")

    LazyColumn(reverseLayout = false,
        contentPadding = PaddingValues(6.dp)) {
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

/**
 * 画像をタップしたときに、画像のURLを返す。
 *
 * @param photo 画像のURL
 * @param onClick タップ時の処理
 *
 * AsyncImageを使い、画像を表示します。
 * modelには、ImageRequest.Builderを使い、画像のURLを指定し、crossfadeを有効にし、ディスクキャッシュ機能を無効にします。
 * placeholderには、画像が読み込まれるまで表示する画像としてnow_loading.pngを指定します。
 * contentScaleには、画像の表示方法を指定します。FillWidthを指定すると、画像の幅を領域いっぱいに表示します。
 */
@Composable
fun PhotoItem(photo: String, onClick: (String) -> Unit) {
    // coil
    // https://coil-kt.github.io/coil/compose/
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
        placeholder = painterResource(id = R.drawable.now_loading),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}
@Preview(showBackground = true)
@Composable
fun PhotoItemPreview() {
    PhotoItem(photo = "https://picsum.photos/seed/1/200", onClick = {})
}


@Composable
fun PhotoGridScreen(onClick: (String) -> Unit) {
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
@Preview(showBackground = true)
@Composable
fun PhotoGridPreview() {
    PhotoGridScreen(){}
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
                .aspectRatio(1f)
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
            placeholder = painterResource(id = R.drawable.now_loading),
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
fun ArticleViewPreview() {
    val message =
        Message(
            "伊賀百貨店は、今月3月25日に大阪市内にオープンしたばかりの新しいお店です。".repeat(5),
            "https://picsum.photos/seed/1/200",
            11
        )
    PostMyStoryTheme {
        ArticleView(message = message)
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    val messages = mutableListOf(
        Message(
            "この鉄橋は、昔の人々の技術と努力の結晶です。長い年月を経てなおしっかりと立っています。".repeat(5),
            "https://picsum.photos/seed/1/200",
            11
        ),
        Message(
            "デジタルとアナログの両方を使って仕事をする人の気持ちを表現しています。".repeat(5),
            "https://picsum.photos/seed/2/200",
            250
        ),
        Message(
            "私は、この場所で心身ともに癒されました。".repeat(5),
            "https://picsum.photos/seed/3/200",
            0
        ),
        Message(
            "先日、友人と一緒にいちご狩りに行ってきました。".repeat(5),
            "https://picsum.photos/seed/4/200",
            462
        )
    )
    PostMyStoryTheme {
        ListScreen(messages = messages){}
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
fun CaptionScreen(onClick:() -> Unit, onChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Column() {
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                onChange(text) },
            label = { Text("キャプションを入力してください") }
        )
        Button(onClick = {
            onClick()
        }) {
            Text(text = "記事を追加する")
        }
    }

}
@Preview(showBackground = true)
@Composable
fun CaptionViewPreview() {
    CaptionScreen(onClick = {}, onChange = {})
}

@Composable
fun CoilTest() {
    AsyncImage(
        model = "https://developer.android.com/static/images/brand/Android_Robot.png",
        contentDescription = null,
    )
}

@Preview
@Composable
fun CoilTestPreview() {
    CoilTest()
}