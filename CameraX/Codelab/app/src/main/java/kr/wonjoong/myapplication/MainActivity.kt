package kr.wonjoong.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import kr.wonjoong.myapplication.databinding.ActivityMainBinding
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Executors.*

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // 권한이 있으면 카메라를 실행하고
        if (allPermissionsGranted()) {
            Log.e("여기", "여기")
            startCamera()
        } else { // 권한이 없으면 권한을 요구한다
            Log.e("여기", "노권한")
            ActivityCompat.requestPermissions(
                this, REQUIRE_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        outputDirectory = getOutputDirectory()
        cameraExecutor = newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // null이 아닌 것을 받을 수 있도록 한다
        val imageCapture = imageCapture ?: return

        // 저장할 사진 파일을 생성한다
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // file과 metadata를 포함한 output option을 생성한다
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 사진 찍는 리스너
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo caputre succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

            }
        )
    }

    private fun startCamera() {
        // CameraX가 lifecycle을 알게되어 카메라를 열고 닫는 task를 줄여주었다.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            // 카메라의 lifecycle을 lifecycle owner에 바인딩 시켜주는 부분

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            // viewFinder는 사용자가 찍을 프리뷰를 보여주기 위해 사용된다.
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

            // 후면 카메라를 디폴트로 잡는다
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // 바인딩 하기 전에 바인딩이 되어 있으면 안되므로 unbinding을 먼저 해준다
                cameraProvider.unbindAll()

                // use case를 카메라에 바인딩한다
                // 그냥 카메라를 preview에 연결하는 것인듯..
                // 맨 마지막에 imageCapture는 option인데 imageCapture를 넣어야 사진 촬영이 가능하다
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalyzer)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
            // 아래에서 Runnable과 getMainExecutor(this)를 통해 Main thread에서 동작할 수 있도록 한다.
        }, ContextCompat.getMainExecutor(this))
    }

    /** REQUIRE_PERMISSIONS의 Permission이 통과되었는지 확인하는 함수 */
    private fun allPermissionsGranted() = REQUIRE_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    // luminosity = 발광성
    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind() // buffer를 0으로 만든다
            val data = ByteArray(remaining())
            get(data)
            return data
        }

        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }

    // 종료될 때 카메라도 종료
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRE_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}