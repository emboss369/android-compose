package com.example.mycamera

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.Locale

data class CameraState(
  val context: Context,
  val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
  val lifecycleOwner: LifecycleOwner,
  val imageCapture: ImageCapture
) {

  fun startCamera(ctx: Context): PreviewView {
    val previewView = PreviewView(ctx).apply {
      implementationMode =
        PreviewView.ImplementationMode.COMPATIBLE
    }

    val executor = ContextCompat.getMainExecutor(ctx)

    cameraProviderFuture.addListener(
      {
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
          it.setSurfaceProvider(previewView.surfaceProvider)
        }

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
            lifecycleOwner, cameraSelector, preview, imageCapture
          )

        } catch (exc: Exception) {
          // このコードには、アプリにフォーカスがなくなった場合など、いくつかの失敗の可能性があります。
          // このコードをcatchブロックでラップして、失敗した場合にログを記録します。
          Log.e(
            "CameraPreview", "Use case binding failed", exc
          )
        }

      }, executor
    )
    return previewView
  }

  private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
  fun takePhoto() {
    // Create time stamped name and MediaStore entry.
    // 次に、画像を格納するMediaStoreのコンテンツバリューを作成します。
    // MediaStoreでの表示名が一意になるように、タイムスタンプを使用します。
    val name = SimpleDateFormat(
      FILENAME_FORMAT,
      Locale.US
    ).format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, name)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
      put(
        MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image"
      )
    }

    /// Create output options object which contains file + metadata
    /// OutputFileOptionsオブジェクトを作成します。このオブジェクトは、どのように出力したいかを指定する場所です。
    /// 他のアプリが表示できるように、出力をMediaStoreに保存したいので、MediaStoreのエントリーを追加してください。
    val outputOptions = ImageCapture.OutputFileOptions.Builder(
      context.contentResolver,
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      contentValues
    ).build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    // imageCaptureオブジェクトでtakePicture()を呼び出します。
    // outputOptions、executor、そして画像が保存されるときのためのコールバックを渡します。
    imageCapture.takePicture(outputOptions,
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
        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
          val msg = "Photo capture succeeded: ${output.savedUri}"
          Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          Log.d("Camera", msg)


          // インテントで共有してみる。
          Intent(Intent.ACTION_SEND).also { share ->
            share.type = "image/*"
            share.putExtra(Intent.EXTRA_STREAM, output.savedUri)
            context.startActivity(
              Intent.createChooser(share, "Share to")
            )
          }
        }
      })
  }
}

@Composable
fun rememberCameraState(
  context: Context = LocalContext.current,
  cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
    ProcessCameraProvider.getInstance(context),
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