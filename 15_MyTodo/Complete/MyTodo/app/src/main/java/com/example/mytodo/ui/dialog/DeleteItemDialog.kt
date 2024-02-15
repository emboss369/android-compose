package com.example.mytodo.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteItemDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = {

    },
    confirmButton = {
      // TextButtonはテキストのみのボタンです。背景色はなく、クリック時にはテキストの色が変わります。
      // 一般的に、ダイアログ内のアクションを実行するために使用されます。
                    TextButton(onClick = {
                      onConfirm()
                    }) {
                        Text(text = "削除")
                    }
    },
    dismissButton = {
      TextButton(onClick = {
        onDismiss()
      }) {
        Text(text = "キャンセル")
      }
    },
    title = { Text(text = "確認") },
    text = { Text(text = "削除しますか？")},
  )
}