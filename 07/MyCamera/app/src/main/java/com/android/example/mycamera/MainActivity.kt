package com.android.example.mycamera

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.android.example.mycamera.ui.theme.MyCameraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCameraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CameraScreen()
                }
            }
        }

//
//        // Request camera permissions
//        if (allPermissionsGranted()) {
//
//
//        } else {
//            ActivityCompat.requestPermissions(
//                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
    }

//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//            baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }

    companion object {
        private const val FILENAME_FORMAT =
            "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}

@Composable
fun CameraScreen(){
    var isGranted by remember { mutableStateOf(false) }

    PermissionHandler { granted ->
        isGranted = granted
    }

    if (isGranted) {
        CameraPreview()
    } else {
        Text(text = "カメラの権限がありません")
    }
}

@Composable
fun CameraPreview() {
    // Context が欲しい Composable で LocalContext.current から Context のインスタンスが取得できます。
    // 参考：Contextについて https://qiita.com/iduchikun/items/34b3ae26cfc438e7e5dc
    val context = LocalContext.current

    // ProcessCameraProviderインスタンスを作成します
    // これは、カメラのライフサイクルをライフサイクル所有者にバインドするために使用されます。
    // CameraX はライフサイクルを認識しているため、カメラを開閉するタスクが不要になります。
    val cameraProviderFuture =
        remember { ProcessCameraProvider.getInstance(context) }

    // LocalLifecycleOwnerは 現在のLifecycleOwnerです。
    // https://developer.android.com/topic/libraries/architecture/lifecycle?hl=ja
    // 現在のLifecycleOwnerは、Android ライフサイクルを持つクラス。
    // これらのイベントをカスタム コンポーネントで使用して、アクティビティまたはフラグメント内に
    // コードを実装せずにライフサイクルの変更を処理できます。
    val lifecycleOwner = LocalLifecycleOwner.current


    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                // PreviewViewの実装モード。
                //PreviewViewがどのようにプレビューをレンダリングするかについてのユーザーの好み。
                // PreviewViewは、SurfaceView（PERFORMANCE）またはTextureView（COMPATIBLE）のどちらかでプレビューを表示します。
                // 消費電力やレイテンシーなど、特定の主要な指標に関しては、一般的にSurfaceViewの方がTextureViewよりも優れています。
                // 一方、TextureViewは、より広い範囲のデバイスでサポートされています。
                // このオプションはPreviewViewによって使用され、デバイスの性能とユーザー設定を考慮して、最適な内部実装を決定します。
                implementationMode =
                    PreviewView.ImplementationMode.COMPATIBLE // テクスチャビューを使います。
            }

            //
            // このコンテキストに関連付けられたメインスレッドで、キューに入れられたタスクを実行するExecutorを返します。
            // このスレッドは、アプリケーションコンポーネント（アクティビティ、サービスなど）の呼び出しをディスパッチ（実行する）するために使用されます。
            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener(
                {
                    // カメラのライフサイクルをライフサイクルオーナにバインドするために使用します。
                    // ProcessCameraProviderインスタンスを作成します
                    // Runnableの中に、ProcessCameraProviderを追加します。これは、カメラのライフサイクルをアプリケーションの
                    // プロセス内のLifecycleOwnerにバインドするために使用されます。
                    val cameraProvider: ProcessCameraProvider =
                        cameraProviderFuture.get()

                    // Preview
                    // プレビューオブジェクトを初期化し、ビルドを呼び出し、ビューファインダーから表面プロバイダを取得し、それをプレビューに設定します。
                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

//                    imageCapture = ImageCapture.Builder()
//                        .build()
                    // バックカメラをデフォルトで選択
                    // CameraSelectorオブジェクトを作成し、DEFAULT_BACK_CAMERAを選択します。
                    val cameraSelector =
                        CameraSelector.DEFAULT_BACK_CAMERA

                    // try ブロックを作成します。そのブロックの中で、cameraProvider に何もバインドされていないことを確認し、
                    // 次に cameraSelector と preview オブジェクトを cameraProvider にバインドしてください。
                    try {
                        // ユースケースのバインドを解除してから再バインドする
                        cameraProvider.unbindAll()

                        // ユースケースをカメラにバインドする
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview
                        )

                    } catch (exc: Exception) {
                        // このコードには、アプリにフォーカスがなくなった場合など、いくつかの失敗の可能性があります。
                        // このコードをcatchブロックでラップして、失敗した場合にログを記録します。
                        Log.e(
                            "CameraPreview",
                            "Use case binding failed",
                            exc
                        )
                    }

                },
                executor
            )


            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyCameraTheme {
        CameraScreen()
    }
}