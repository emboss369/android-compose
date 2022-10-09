package com.android.example.cameraxapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.example.cameraxapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        // 「newSingleThreadExecutor」でスレッドを一つのみ生成。ImageAnalysis用で今は使ってない。
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        // まず、ImageCaptureユースケースへの参照を取得します。ユースケースがnullの場合は、関数を終了します。
        // これは、イメージキャプチャが設定される前に写真ボタンをタップした場合にnullになります。
        // return文がないと、nullの場合、アプリがクラッシュしてしまいます。
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        // 次に、画像を格納するMediaStoreのコンテンツバリューを作成します。
        // MediaStoreでの表示名が一意になるように、タイムスタンプを使用します。
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        // OutputFileOptionsオブジェクトを作成します。このオブジェクトは、どのように出力したいかを指定する場所です。
        // 他のアプリが表示できるように、出力をMediaStoreに保存したいので、MediaStoreのエントリーを追加してください。
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        // imageCaptureオブジェクトでtakePicture()を呼び出します。
        // outputOptions、executor、そして画像が保存されるときのためのコールバックを渡します。
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                // 画像キャプチャに失敗した場合や画像キャプチャの保存に失敗した場合は、失敗したことを記録するエラーケースを追加してください。
                override fun onError(exc: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exc.message}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, msg, exc)
                }

                // キャプチャに失敗しなければ、写真の撮影は成功です 先ほど作成したファイルに写真を保存し、
                // 成功したことを知らせるためにトーストを表示し、ログ文を出力します。
                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)




                    // インテントで共有してみる。
                    Intent(Intent.ACTION_SEND).also { share ->
                        share.type = "image/*"
                        share.putExtra(Intent.EXTRA_STREAM, output.savedUri)
                        startActivity(Intent.createChooser(share, "Share to"))
                    }
                }
            }
        )
    }

    private fun captureVideo() {}


    // https://developer.android.com/codelabs/camerax-getting-started?hl=ja#3
    private fun startCamera() {
        // ProcessCameraProviderインスタンスを作成します
        // これは、カメラのライフサイクルをライフサイクル所有者にバインドするために使用されます。CameraX はライフサイクルを認識しているため、カメラを開閉するタスクが不要になります。
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // cameraProviderFutureにリスナーを追加します
        // 第1引数にRunnableを追加します。
        // 第2引数にContextCompat.getMainExecutor()を追加する。これは、メインスレッドで実行されるExecutorを返します。
        //
        cameraProviderFuture.addListener({
            // カメラのライフサイクルをライフサイクルオーナにバインドするために使用します。
            // ProcessCameraProviderインスタンスを作成します
            // Runnableの中に、ProcessCameraProviderを追加します。これは、カメラのライフサイクルをアプリケーションの
            // プロセス内のLifecycleOwnerにバインドするために使用されます。
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            // プレビューオブジェクトを初期化し、ビルドを呼び出し、ビューファインダーから表面プロバイダを取得し、それをプレビューに設定します。
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()


            // バックカメラをデフォルトで選択
            // CameraSelectorオブジェクトを作成し、DEFAULT_BACK_CAMERAを選択します。
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // try ブロックを作成します。そのブロックの中で、cameraProvider に何もバインドされていないことを確認し、
            // 次に cameraSelector と preview オブジェクトを cameraProvider にバインドしてください。
            try {
                // ユースケースのバインドを解除してから再バインドする
                cameraProvider.unbindAll()

                // ユースケースをカメラにバインドする
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                // このコードには、アプリにフォーカスがなくなった場合など、いくつかの失敗の可能性があります。
                // このコードをcatchブロックでラップして、失敗した場合にログを記録します。
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}