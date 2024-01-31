package com.example.webpageapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScrollableTabRowView(tabIndex: Int, onTabChange: (Int) -> Unit) {
  val tabs = listOf(
    "Home" to Icons.Default.Home,
    "About" to Icons.Default.Info,
    "Settings" to Icons.Default.Settings,
    "User" to Icons.Default.Person,
    "Nice" to Icons.Default.ThumbUp,
    "Email" to Icons.Default.Email,
    "Star" to Icons.Default.Star,
    "Menu" to Icons.Default.Menu
  )

  ScrollableTabRow(selectedTabIndex = tabIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(text = { Text(tab.first) },
        selected = tabIndex == index,
        onClick = { onTabChange(index) },
        icon = {
          Icon(
            imageVector = tab.second, contentDescription = tab.first
          )
        }
      )
    }
  }
}

@Preview
@Composable
fun ScrollableTabRowViewPreview() {
  ScrollableTabRowView(0, onTabChange = { })
}