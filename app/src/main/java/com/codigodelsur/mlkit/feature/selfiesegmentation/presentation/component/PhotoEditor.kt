package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions

@Composable
fun PhotoEditor(
    modifier: Modifier = Modifier,
    photo: Bitmap,
    onPhotoEdit: (Bitmap) -> Unit,
    onResetClick: () -> Unit,
    onCloseClick: () -> Unit,
    onEditionError: (Throwable) -> Unit
) {
    val segmenter = remember {
        Segmentation.getClient(
            SelfieSegmenterOptions.Builder()
                .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                .build()
        )
    }
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = photo.asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp),
            onClick = onCloseClick
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
                    val image = InputImage.fromBitmap(photo, 0)
                    segmenter.process(image)
                        .addOnSuccessListener { mask ->
                            mask?.let {
                                val modifiedPhoto = removeBackground(photo, mask)
                                onPhotoEdit(modifiedPhoto)
                            }
                        }
                        .addOnFailureListener { th ->
                            onEditionError(th)
                        }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.selfie_segmentation_remove_background)
                )
            }

            Button(
                onClick = onResetClick
            ) {
                Text(
                    text = stringResource(id = R.string.selfie_segmentation_reset)
                )
            }
        }
    }
}

private fun removeBackground(originalBitmap: Bitmap, mask: SegmentationMask): Bitmap {
    // Create a mutable bitmap to draw the result
    val resultBitmap = Bitmap.createBitmap(
        originalBitmap.width,
        originalBitmap.height,
        Bitmap.Config.ARGB_8888
    )

    // Get mask buffer and prepare for use
    val maskBuffer = mask.buffer.apply { rewind() }
    val pixels = IntArray(originalBitmap.width * originalBitmap.height)
    originalBitmap.getPixels(
        pixels,
        0,
        originalBitmap.width,
        0,
        0,
        originalBitmap.width,
        originalBitmap.height
    )

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
    resultBitmap.setPixels(
        pixels,
        0,
        originalBitmap.width,
        0,
        0,
        originalBitmap.width,
        originalBitmap.height
    )

    return resultBitmap
}

@Preview
@Composable
private fun PhotoEditorPreview() {
    MlkTheme {
        PhotoEditor(
            modifier = Modifier.fillMaxSize(),
            photo = BitmapFactory.decodeResource(
                LocalContext.current.resources,
                R.drawable.clown_nose
            ),
            onPhotoEdit = {},
            onResetClick = {},
            onCloseClick = {},
            onEditionError = {},
        )
    }
}