package com.codigodelsur.mlkit.feature.posedetection.presentation.analyzer

import android.content.Context
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.codigodelsur.mlkit.feature.posedetection.presentation.classification.PoseClassificationResult
import com.codigodelsur.mlkit.feature.posedetection.presentation.classification.PoseClassifierProcessor
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.PPose
import com.codigodelsur.mlkit.feature.posedetection.presentation.model.toPresentation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PoseDetectionAnalyzer(
    context: Context,
    private val onPoseDetected: (PPose?, PoseClassificationResult?, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector = PoseDetection.getClient(options)
    private val poseClassifier = PoseClassifierProcessor(context, true)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)


            suspendCoroutine { continuation ->
                poseDetector.process(inputImage)
                    .addOnSuccessListener { pose: Pose ->
                        val classification = poseClassifier.getPoseResult(pose)
                        onPoseDetected(
                            pose.toPresentation(),
                            classification,
                            inputImage.width,
                            inputImage.height
                        )
                    }
                    .addOnFailureListener {
                        // We could propagate the error to show a message if needed
                        it.printStackTrace()
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}
