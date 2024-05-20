package com.codigodelsur.mlkit.feature.objectdetection.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.objectdetection.presentation.component.ObjectBoundingBoxesOverlay
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

@Composable
fun ObjectDetectionRoute(
    modifier: Modifier = Modifier,
    viewModel: ObjectDetectionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObjectDetectionScreen(
        modifier = modifier,
        hideUnlabeled = state.hideUnlabeled,
        detectedObjects = state.detectedObjects,
        onObjectDetected = { objects ->
            viewModel.updateDetectedObjects(objects)
        },
        onToggleHideUnlabeled = {
            viewModel.toggleHideUnlabeled()
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ObjectDetectionScreen(
    modifier: Modifier = Modifier,
    hideUnlabeled: Boolean,
    detectedObjects: List<DetectedObject>,
    onObjectDetected: (List<DetectedObject>) -> Unit,
    onToggleHideUnlabeled: () -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnObjectDetected by rememberUpdatedState(onObjectDetected)
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_object_detection_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MlkCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    setUpDetector = { cameraController, context ->
                        // Default object detector
//                        val options = ObjectDetectorOptions.Builder()
//                            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//                            .enableMultipleObjects()
//                            .enableClassification()
//                            .build()
//                        val objectDetector = ObjectDetection.getClient(options)

                        // Custom object detector
                        // For more information on how to download compatible pre-trained models or
                        // training your own, visit: https://developers.google.com/ml-kit/custom-models
                        val localModel = LocalModel.Builder()
                            .setAssetFilePath("object/efficientnet.tflite")
                            .build()
                        val customObjectDetectorOptions =
                            CustomObjectDetectorOptions.Builder(localModel)
                                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                                .enableMultipleObjects()
                                .enableClassification()
                                .build()
                        val objectDetector =
                            ObjectDetection.getClient(customObjectDetectorOptions)

                        val executor = ContextCompat.getMainExecutor(context)
                        cameraController.setImageAnalysisAnalyzer(
                            executor,
                            MlKitAnalyzer(
                                listOf(objectDetector),
                                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                                executor
                            ) { result: MlKitAnalyzer.Result? ->
                                val objects = result?.getValue(objectDetector)
                                currentOnObjectDetected(objects.orEmpty())
                            }
                        )
                    }
                ) {
                    ObjectBoundingBoxesOverlay(
                        modifier = Modifier.fillMaxSize(),
                        hideUnlabeled = hideUnlabeled,
                        objects = detectedObjects
                    )
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                    onClick = onToggleHideUnlabeled
                ) {
                    Text(
                        text = stringResource(
                            id = if (hideUnlabeled) {
                                R.string.object_detection_show_all
                            } else {
                                R.string.object_detection_hide_unlabeled
                            }
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TextRecognitionScreenPreview() {
    MlkTheme {
        ObjectDetectionScreen(
            hideUnlabeled = false,
            detectedObjects = listOf(),
            onObjectDetected = { _ -> },
            onToggleHideUnlabeled = {},
            onBackClick = {}
        )
    }
}