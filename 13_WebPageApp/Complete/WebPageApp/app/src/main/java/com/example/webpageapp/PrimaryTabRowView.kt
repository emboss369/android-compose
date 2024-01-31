package com.example.webpageapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.webpageapp.ui.theme.WebPageAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTabRowView(tabIndex: Int, onTabChange: (Int) -> Unit) {

  val tabs = listOf(
    "Home" to Icons.Default.Home,
    "About" to Icons.Default.Info,
    "Settings" to Icons.Default.Settings
  )

  PrimaryTabRow(selectedTabIndex = tabIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(text = { Text(tab.first) },
        selected = tabIndex == index,
        onClick = { onTabChange(index) },
        icon = {
          Icon(
            imageVector = tab.second, contentDescription = tab.first
          )
        })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PrimaryTabRowViewPreview() {
  WebPageAppTheme {
    PrimaryTabRowView(0, onTabChange = { })
  }
}