package com.example.animation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.animation.ui.theme.AnimationTheme

@Composable
fun ArticleDetail(article: Article) {
    val state = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(state)
    ) {

    }
}
@Preview(showBackground = true)
@Composable
fun ArticleDetailPreview() {
    AnimationTheme {
        val article = Article(
            title = "タイトル",
            caption = "キャプション",
            image = "https://picsum.photos/seed/5/200"
        )
        ArticleDetail(article = article)
    }
}