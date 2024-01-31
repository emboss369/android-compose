package com.example.webpageapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webpageapp.ui.theme.WebPageAppTheme

@Composable
fun TabView(tabIndex: Int, onTabChange: (Int) -> Unit) {

  val tabs = listOf("Home", "About", "Settings")

  Column {
    Column(modifier = Modifier.fillMaxWidth()) {
      TabRow(selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, title ->
          Tab(text = { Text(title) },
            selected = tabIndex == index,
            onClick = { onTabChange(index) },
            icon = {
              when (index) {
                0 -> Icon(
                  imageVector = Icons.Default.Home, contentDescription = null
                )

                1 -> Icon(
                  imageVector = Icons.Default.Info, contentDescription = null
                )

                2 -> Icon(
                  imageVector = Icons.Default.Settings,
                  contentDescription = null
                )
              }
            })
        }
      }

    }

  }
}

@Preview(showBackground = true)
@Composable
fun TabViewPreview() {
  WebPageAppTheme {
    TabView( 0, onTabChange = { })
  }
}