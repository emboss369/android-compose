package com.example.animation

import androidx.compose.runtime.Stable

@Stable // https://developer.android.com/jetpack/compose/lifecycle?hl=ja
data class Article(
    val title: String,
    val caption: String,
    val image: String,
)