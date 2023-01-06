package com.example.animation

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animation.ui.theme.AnimationTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleListView(articles: MutableList<Article>) {
    val state = rememberLazyListState()


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {


        LazyColumn(
            // アイテム間の間隔
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // コンテンツの端にパディングを追加
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            state = state
        ) {
            // itemsを使うには
            // import androidx.compose.foundation.lazy.items
            // を明示的にインポートする必要がある。
            items(articles) { article ->
                ArticleView(
                    // アイテムの変更をアニメーション化できます。
                    // API はシンプルで、animateItemPlacement 修飾子を
                    // アイテムのコンテンツに設定するだけです。
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessHigh,
                            visibilityThreshold = null
                        )
                    ),
                    article = article
                )
            }
        }
        FloatingActionButton(onClick = {
            articles.add( Article(
                title = "New Title",
                caption = "New Caption",
                image = "https://picsum.photos/seed/${articles.count()}/200)"))
            Log.d("hoge","${articles[0].image}")
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleListViewPreview() {
    AnimationTheme {
        var articles = remember{
            mutableStateListOf(
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/1/200"
                ),
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/2/200"
                ),
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/3/200"
                ),
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/4/200"
                ),
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/5/200"
                ),
                Article(
                    title = "タイトル",
                    caption = "キャプション",
                    image = "https://picsum.photos/seed/6/200"
                )
            )
        }
        ArticleListView(articles = articles)
    }
}