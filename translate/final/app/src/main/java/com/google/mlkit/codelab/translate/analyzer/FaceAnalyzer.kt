package com.google.mlkit.codelab.translate.analyzer

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.mlkit.codelab.translate.util.ImageUtils
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.text.Text
import java.lang.Exception

class FaceAnalyzer(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val isSmiling: MutableLiveData<Boolean>,
    private val openEyes: MutableLiveData<String>
) : ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient()

    init {
        lifecycle.addObserver(detector)
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        recognizeFace(image).addOnCompleteListener {
            imageProxy.close()
        }
    }

    private fun recognizeFace(
        image: InputImage
    ): Task<MutableList<Face>> {
        // Pass image to an ML Kit Vision API
        return detector.process(image)
            .addOnSuccessListener { list ->
                val face = list.firstOrNull()
                isSmiling.value = face?.smilingProbability ?: 0.0f >= 0.5f
                openEyes.value = getEyesMessage(face?.leftEyeOpenProbability?:0.0f, face?.rightEyeOpenProbability?:0.0f)
                Log.d(TAG, "Face detection success ${openEyes.value}")
            }
            .addOnFailureListener {
                val message = getErrorMessage(it)
                openEyes.value = message
                Log.e(TAG, "Face detection failure", it)
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun getEyesMessage(lx: Float, rx: Float): String{
        return when{
            lx < 0.5f && rx < 0.5f -> "Occhi chiusi"
            rx < 0.5f -> "Occhio destro chiuso"
            lx < 0.5f -> "Occhio sinistro chiuso"
            else -> "Occhi aperti"
        }
    }

    private fun getErrorMessage(exception: Exception): String? {
        val mlKitException = exception as? MlKitException ?: return exception.message
        return if (mlKitException.errorCode == MlKitException.UNAVAILABLE) {
            "Waiting for text recognition model to be downloaded"
        } else exception.message
    }

    companion object {
        private const val TAG = "FaceAnalyzer"
    }
}