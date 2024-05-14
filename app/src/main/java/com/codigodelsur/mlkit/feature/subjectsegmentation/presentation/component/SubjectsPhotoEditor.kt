package com.codigodelsur.mlkit.feature.subjectsegmentation.presentation.component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.util.getForId
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import java.nio.FloatBuffer

@Composable
fun SubjectsPhotoEditor(
    modifier: Modifier = Modifier,
    photo: Bitmap,
    onCloseClick: () -> Unit,
    onEditionError: (Throwable) -> Unit
) {
    var modifiedPhoto by remember { mutableStateOf(photo) }
    var loading by remember { mutableStateOf(false) }
    val foregroundSegmenter = remember {
        SubjectSegmentation.getClient(
            SubjectSegmenterOptions.Builder()
                .enableForegroundBitmap()
                .build()
        )
    }
    val subjectsSegmenter = remember {
        SubjectSegmentation.getClient(
            SubjectSegmenterOptions.Builder()
                .enableMultipleSubjects(
                    SubjectSegmenterOptions.SubjectResultOptions.Builder()
                        .enableConfidenceMask()
                        .build()
                )
                .build()
        )
    }

    if (loading) {
        Dialog(
            onDismissRequest = { loading = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }

    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = modifiedPhoto.asImageBitmap(),
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
                    loading = true
                    val image = InputImage.fromBitmap(photo, 0)
                    foregroundSegmenter.process(image)
                        .addOnSuccessListener { result ->
                            result?.let {
                                result.foregroundBitmap?.let {
                                    modifiedPhoto = it
                                } ?: run {
                                    onEditionError(IllegalStateException("Unable to get foreground bitmap"))
                                }
                            }
                        }
                        .addOnFailureListener(onEditionError)
                        .addOnCompleteListener { loading = false }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.subject_segmentation_show_foreground_bitmap)
                )
            }

            Button(
                onClick = {
                    loading = true
                    val image = InputImage.fromBitmap(photo, 0)
                    subjectsSegmenter.process(image)
                        .addOnSuccessListener { result ->
                            if (result.subjects.isNotEmpty()) {
                                val overlays = result.subjects.mapIndexedNotNull { index, subject ->
                                    subject.confidenceMask?.let { mask ->
                                        floatBufferToBitmap(
                                            floatBuffer = mask,
                                            width = subject.width,
                                            height = subject.height,
                                            color = Color.getForId(index).copy(alpha = 0.8f)
                                        ) to Point(subject.startX, subject.startY)
                                    }
                                }
                                modifiedPhoto = overlayBitmaps(photo, overlays)
                            } else {
                                onEditionError(IllegalStateException("No subjects segmented"))
                            }
                        }
                        .addOnFailureListener(onEditionError)
                        .addOnCompleteListener { loading = false }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.subject_segmentation_show_subject_masks)
                )
            }

            Button(
                onClick = { modifiedPhoto = photo }
            ) {
                Text(
                    text = stringResource(id = R.string.reset)
                )
            }
        }
    }
}

private fun floatBufferToBitmap(
    floatBuffer: FloatBuffer,
    width: Int,
    height: Int,
    color: Color
): Bitmap {
    floatBuffer.rewind()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val colors = IntArray(width * height)

    for (i in colors.indices) {
        val confidence = floatBuffer.get()
        colors[i] = if (confidence > 0.5f) {
            color
        } else {
            Color.Transparent
        }.toArgb()
    }

    bitmap.setPixels(colors, 0, width, 0, 0, width, height)
    return bitmap
}

fun overlayBitmaps(originalBitmap: Bitmap, masks: List<Pair<Bitmap, Point>>): Bitmap {
    val resultBitmap = Bitmap.createBitmap(originalBitmap.width, originalBitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resultBitmap)
    canvas.drawBitmap(originalBitmap, 0f, 0f, null)

    val paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER) // Overlay mode
    }
    masks.forEach { (bitmap, start) ->
        canvas.drawBitmap(bitmap, start.x.toFloat(), start.y.toFloat(), paint)
    }
    return resultBitmap
}