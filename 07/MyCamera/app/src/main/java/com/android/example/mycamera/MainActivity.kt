package com.android.example.mycamera

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.android.example.mycamera.ui.theme.MyCameraTheme
import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.*

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
    }
}

data class CameraState(
    val context: Context,
    val cameraProviderFuture:
    ListenableFuture<ProcessCameraProvider>,
    val lifecycleOwner: LifecycleOwner,
    val imageCapture: ImageCapture
) {
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun startCamera(ctx: Context): PreviewView {
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

        // ProcessCameraProviderインスタンスです。
        // これは、カメラのライフサイクルをライフサイクル所有者にバインドするために使用されます。
        // CameraX はライフサイクルを認識しているため、カメラを開閉するタスクが不要になります。
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
                        lifecycleOwner, cameraSelector,
                        preview, imageCapture
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


        return previewView
    }

    fun takePhoto() {
        // Create time stamped name and MediaStore entry.
        // 次に、画像を格納するMediaStoreのコンテンツバリューを作成します。
        // MediaStoreでの表示名が一意になるように、タイムスタンプを使用します。
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/CameraX-Image"
                )
            }
        }

        /// Create output options object which contains file + metadata
        /// OutputFileOptionsオブジェクトを作成します。このオブジェクトは、どのように出力したいかを指定する場所です。
        /// 他のアプリが表示できるように、出力をMediaStoreに保存したいので、MediaStoreのエントリーを追加してください。
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        // imageCaptureオブジェクトでtakePicture()を呼び出します。
        // outputOptions、executor、そして画像が保存されるときのためのコールバックを渡します。
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                // 画像キャプチャに失敗した場合や画像キャプチャの保存に失敗した場合は、失敗したことを記録するエラーケースを追加してください。
                override fun onError(exc: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exc.message}"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.e("Camera", msg, exc)
                }

                // キャプチャに失敗しなければ、写真の撮影は成功です 先ほど作成したファイルに写真を保存し、
                // 成功したことを知らせるためにトーストを表示し、ログ文を出力します。
                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d("Camera", msg)


                    // インテントで共有してみる。
                    Intent(Intent.ACTION_SEND).also { share ->
                        share.type = "image/*"
                        share.putExtra(Intent.EXTRA_STREAM, output.savedUri)
                        context.startActivity(
                            Intent
                                .createChooser(share, "Share to")
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun rememberCameraState(
    // Context が欲しい Composable で LocalContext.current から Context のインスタンスが取得できます。
    // 参考：Contextについて https://qiita.com/iduchikun/items/34b3ae26cfc438e7e5dc
    context: Context = LocalContext.current,
    // ProcessCameraProviderインスタンスを作成します
    // これは、カメラのライフサイクルをライフサイクル所有者にバインドするために使用されます。
    // CameraX はライフサイクルを認識しているため、カメラを開閉するタスクが不要になります。
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    = ProcessCameraProvider.getInstance(context),
    // LocalLifecycleOwnerは 現在のLifecycleOwnerです。
    // https://developer.android.com/topic/libraries/architecture/lifecycle?hl=ja
    // 現在のLifecycleOwnerは、Android ライフサイクルを持つクラス。
    // これらのイベントをカスタム コンポーネントで使用して、アクティビティまたはフラグメント内に
    // コードを実装せずにライフサイクルの変更を処理できます。
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,

    imageCapture: ImageCapture = ImageCapture.Builder().build()
) = remember(context, cameraProviderFuture, lifecycleOwner) {
    CameraState(
        context = context,
        cameraProviderFuture = cameraProviderFuture,
        lifecycleOwner = lifecycleOwner,
        imageCapture = imageCapture
    )
}

@Composable
fun CameraScreen() {
    var isGranted by remember { mutableStateOf(false) }

    val cameraState = rememberCameraState()

    PermissionHandler { granted ->
        isGranted = granted
    }

    if (isGranted) {
        Box() {
            CameraPreview { ctx ->
                cameraState.startCamera(ctx)
            }
            TakePhoto {
                cameraState.takePhoto()
            }
        }
    } else {
        Text(text = "カメラの権限がありません")
    }
}
@Composable
fun TakePhoto(takePhoto: () -> Unit) {

    Button(onClick = {
        takePhoto()
    }) {
        Text(text = "Picture")
    }
}
@Composable
fun CameraPreview(createCameraPreview: (Context) -> PreviewView) {
        AndroidView(
            factory = { ctx ->
                createCameraPreview(ctx)
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
