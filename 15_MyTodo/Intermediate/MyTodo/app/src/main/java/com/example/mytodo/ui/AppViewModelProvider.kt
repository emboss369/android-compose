package com.example.mytodo.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytodo.TodoApplication
import com.example.mytodo.ui.home.HomeViewModel
import com.example.mytodo.ui.item.ItemEditViewModel
import com.example.mytodo.ui.item.ItemEntryViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 * Inventory アプリ全体の ViewModel インスタンスを作成するファクトリーを提供する。
 */

// object はシングルトン
// シングルトンは非常に便利なパターンであり、Kotlin（Scalaの後です）は、シングルトンを容易に宣言で切るようにしました
// オブジェクト宣言の構文はシンプルです。クラス名の前にclassではなく「object」と付け加えるだけ。
// これでインスタンスを1つしか持たない、シングルトンオブジェクトが定義できました。
// 「AppViewModelProvider」はクラス名でもあり、ただ1つのインスタンス名でもあります。
object AppViewModelProvider {

  val Factory = viewModelFactory {

    initializer {


      ItemEditViewModel(
        /**
         ViewModelのデータを保存/復元するためSavedStateHandle の新しいインスタンスを作成するには、
         [CreationExtras].createSavedStateHandle().createSavedStateHandle()
         関数を使用して ViewModel に渡します。
         */
        this.createSavedStateHandle(),
        todoApplication().container.itemsRepository
      )
    }

    initializer {
      ItemEntryViewModel(todoApplication().container.itemsRepository)
    }

    // Initializer for HomeViewModel
    initializer {
      HomeViewModel(todoApplication().container.itemsRepository)
    }
  }
}

// CreationExtrasから、Applicationを取得できます。
fun CreationExtras.todoApplication(): TodoApplication =
  (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)