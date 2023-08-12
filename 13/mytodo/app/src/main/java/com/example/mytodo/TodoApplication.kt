package com.example.mytodo

import android.app.Application
import com.example.mytodo.data.AppContainer
import com.example.mytodo.data.AppDataMemoryContainer


/**
 * Android Manifestに登録しておきます。
 *     <application android:name=".TodoApplication" を追加します。
 *
 * TodoApplicationはアプリ「全体」のことと捉えてください。
 * Androidアプリが起動した時に最初にインスタンスが作られるクラスです。
 * このクラスでは、アプリ全体で用いる設定をこのクラスで行います。
 * AndroidManifestに登録しないと、このクラスは生成されませんので注意が必要。
 * ここでは、AppDataMemoryContainerを生成して保持しています。
 */
class TodoApplication : Application() {

  // 他のクラスが依存関係を取得するために使用するAppContainerインスタンス。
  lateinit var container: AppContainer
  override fun onCreate() {
    super.onCreate()
    container = AppDataMemoryContainer(this)
  }
}