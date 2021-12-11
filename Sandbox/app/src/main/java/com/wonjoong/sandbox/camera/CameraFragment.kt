package com.wonjoong.sandbox.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.wonjoong.sandbox.R
import com.wonjoong.sandbox.databinding.FragmentCameraBinding
import com.wonjoong.sandbox.util.BaseFragment
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.concurrent.Executors

class CameraFragment : BaseFragment<FragmentCameraBinding>(R.layout.fragment_camera) {

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Request camera permissions
        requestPermission()
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener({
            // lifecycleowner에 맞춰 camera lifecycle을 맞추기 위해 추가하는 코드
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // 프리뷰
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, CustomImageAnalyzer(this::setRecognizedText))
                }
            // 후면 카메라 Default 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // 다시 바인딩 하기 전에 unbind
                cameraProvider.unbindAll()
                // 카메라 바인딩
                cameraProvider.bindToLifecycle(
                    requireActivity(),
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("Camera Fragment", "Use case binding failed", exc)
            }
            // 메인 쓰레드에서 도는 Executor 설정
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun setRecognizedText(recognizedText: String) {
        lifecycleScope.launchWhenStarted {
            binding.tvResult.text = recognizedText
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private class CustomImageAnalyzer(
        private val setRecognizedText: (String) -> Unit
    ) : ImageAnalysis.Analyzer {

        private val recognitionKoreanOption = KoreanTextRecognizerOptions.Builder().build()
        private val recognizer = TextRecognition.getClient(recognitionKoreanOption)

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(image: ImageProxy) {
            val mediaImage = image.image
            if (mediaImage != null) {
                val newImage =
                    InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                detectInImage(newImage)
                    .addOnSuccessListener { newRecognizedText ->
                        setRecognizedText(newRecognizedText.text)
                    }
                    .addOnFailureListener {
                        Log.e("onFailure", "exception : $it")
                    }
                    .addOnCompleteListener {
                        image.close()
                    }
            }
        }

        private fun detectInImage(image: InputImage): Task<Text> {
            return recognizer.process(image)
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}