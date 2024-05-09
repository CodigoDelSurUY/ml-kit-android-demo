package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import java.nio.ByteBuffer

@Composable
fun SelfieSegmentationRoute(
    modifier: Modifier = Modifier,
    viewModel: SelfieSegmentationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SelfieSegmentationScreen(
        modifier = modifier,
        selfieMask = state.selfieMask,
        foregroundThreshold = state.foregroundThreshold,
        onSelfieSegmented = viewModel::updateSegmentedMask,
        onBackClick = onBackClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelfieSegmentationScreen(
    modifier: Modifier,
    selfieMask: SegmentationMask?,
    foregroundThreshold: Float,
    onSelfieSegmented: (SegmentationMask?) -> Unit,
    onBackClick: () -> Unit
) {
    var takenPhoto by remember { mutableStateOf<Bitmap?>(null) }
    var modifiedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    val currentOnSelfieSegmented by rememberUpdatedState(onSelfieSegmented)
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_selfie_segmentation_title,
            onNavigationClick = onBackClick
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (takenPhoto == null || modifiedPhoto == null) {
                    MlkCameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        isCapturePhotoEnabled = true,
                        onPhotoCaptured = {
                            takenPhoto = it
                            modifiedPhoto = it
                        },
                        setUpDetector = { cameraController, context ->
                            val options =
                                SelfieSegmenterOptions.Builder()
                                    .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
                                    .enableRawSizeMask()
                                    .build()
                            val segmenter = Segmentation.getClient(options)
                            val executor = ContextCompat.getMainExecutor(context)
                            cameraController.setImageAnalysisAnalyzer(
                                executor,
                                MlKitAnalyzer(
                                    listOf(segmenter),
                                    // ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED Does not work with segmentation so we need to calculate everything ourselves
                                    ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL,
                                    executor
                                ) { result: MlKitAnalyzer.Result? ->
                                    val mask = result?.getValue(segmenter)
                                    currentOnSelfieSegmented(mask)
                                }
                            )
                        }
                    ) {
                        if (selfieMask != null) {
                            SelfieMaskOverlay(
                                modifier = Modifier.fillMaxSize(),
                                foregroundThreshold = foregroundThreshold,
                                selfieMask = selfieMask
                            )
                        }
                    }
                } else {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = modifiedPhoto!!.asImageBitmap(),
                        contentDescription = ""
                    )

                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.dp, top = 16.dp),
                        onClick = {
                            takenPhoto = null
                            modifiedPhoto = null
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24),
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val options =
                                    SelfieSegmenterOptions.Builder()
                                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                                        .build()
                                val segmenter = Segmentation.getClient(options)
                                val image = InputImage.fromBitmap(takenPhoto!!, 0)
                                segmenter.process(image)
                                    .addOnSuccessListener { mask ->
                                        mask?.let {
                                            modifiedPhoto = removeBackground(takenPhoto!!, mask)
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // Task failed with an exception
                                        // ...
                                    }
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.selfie_segmentation_remove_background)
                            )
                        }

                        Button(
                            onClick = {
                                modifiedPhoto = takenPhoto
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.selfie_segmentation_reset)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun removeBackground(originalBitmap: Bitmap, mask: SegmentationMask): Bitmap {
    // Create a mutable bitmap to draw the result
    val resultBitmap = Bitmap.createBitmap(
        originalBitmap.width,
        originalBitmap.height,
        Bitmap.Config.ARGB_8888
    )

    // Get mask buffer and prepare for use
    val maskBuffer = mask.buffer.apply { rewind() }
    val pixels = IntArray(originalBitmap.width * originalBitmap.height)
    originalBitmap.getPixels(pixels, 0, originalBitmap.width, 0, 0, originalBitmap.width, originalBitmap.height)

    // Apply the mask
    for (y in 0 until originalBitmap.height) {
        for (x in 0 until originalBitmap.width) {
            val pixelIndex = y * originalBitmap.width + x
            val confidence = maskBuffer.float // Read each float confidence value

            if (confidence > 0.5) {
                // Keep original pixel
                pixels[pixelIndex] = pixels[pixelIndex]
            } else {
                // Set pixel to transparent
                pixels[pixelIndex] = 0
            }
        }
    }

    // Draw the modified pixels array to the canvas
    resultBitmap.setPixels(pixels, 0, originalBitmap.width, 0, 0, originalBitmap.width, originalBitmap.height)

    return resultBitmap
}

@Composable
private fun SelfieMaskOverlay(
    modifier: Modifier,
    selfieMask: SegmentationMask,
    foregroundThreshold: Float
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
    ) {
        drawSegmentationMask(
            buffer = selfieMask.buffer,
            width = selfieMask.width,
            height = selfieMask.height,
            foregroundThreshold = foregroundThreshold
        )
    }
}

private fun DrawScope.drawSegmentationMask(
    buffer: ByteBuffer,
    width: Int,
    height: Int,
    foregroundThreshold: Float
) {
    val scaleX = size.width / width
    val scaleY = size.height / height
    buffer.rewind()
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color =
                if (buffer.getFloat() > foregroundThreshold) Color.Red.copy(alpha = 0.8f) else Color.Transparent
            val positionX = size.width - scaleX * x // Mirrored due to front camera
            val positionY = y * scaleY
            drawRect(
                color = color,
                topLeft = Offset(positionX, positionY),
                size = Size(scaleX, scaleY)
            )
        }
    }
}
