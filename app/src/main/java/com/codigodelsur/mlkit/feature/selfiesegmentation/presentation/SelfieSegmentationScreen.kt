package com.codigodelsur.mlkit.feature.selfiesegmentation.presentation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.component.ShowSnackbarEffect
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.analyzer.SelfieSegmentationAnalyzer
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.component.SelfiePhotoEditor
import com.codigodelsur.mlkit.feature.selfiesegmentation.presentation.component.SelfieMaskOverlay
import com.google.mlkit.vision.segmentation.SegmentationMask

@Composable
fun SelfieSegmentationRoute(
    modifier: Modifier = Modifier,
    viewModel: SelfieSegmentationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.editorError?.let {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(it)) {
            viewModel.hideEditorError()
        }
    }

    SelfieSegmentationScreen(
        modifier = modifier,
        selfieMask = state.selfieMask,
        foregroundThreshold = state.foregroundThreshold,
        onSelfieSegmented = viewModel::updateSegmentedMask,
        onEditorError = viewModel::showEditorError,
        onBackClick = onBackClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelfieSegmentationScreen(
    modifier: Modifier = Modifier,
    selfieMask: SegmentationMask?,
    foregroundThreshold: Float,
    onSelfieSegmented: (SegmentationMask?) -> Unit,
    onEditorError: (Throwable) -> Unit,
    onBackClick: () -> Unit
) {
    var takenPhoto by remember { mutableStateOf<Bitmap?>(null) }
    val currentOnSelfieSegmented by rememberUpdatedState(onSelfieSegmented)

    BackHandler(enabled = takenPhoto != null) {
        takenPhoto = null
    }

    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_selfie_segmentation_title,
            onNavigationClick = {
                if (takenPhoto != null) {
                    takenPhoto = null
                } else {
                    onBackClick()
                }
            }
        )
        CameraPermissionRequester(
            modifier = Modifier.weight(1.0f)
        ) {
            if (takenPhoto == null) {
                MlkCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    scaleType = PreviewView.ScaleType.FIT_CENTER,  // Needed to position the overlay correctly
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
                    onPhotoCapture = { takenPhoto = it },
                    setUpDetector = { cameraController, context ->
                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            SelfieSegmentationAnalyzer(onSelfieSegmented = currentOnSelfieSegmented)
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
                SelfiePhotoEditor(
                    modifier = Modifier.fillMaxSize(),
                    photo = takenPhoto!!,
                    onCloseClick = { takenPhoto = null },
                    onEditionError = onEditorError
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelfieSegmentationScreenPreview() {
    MlkTheme {
        SelfieSegmentationScreen(
            selfieMask = null,
            foregroundThreshold = 0.5f,
            onSelfieSegmented = {},
            onEditorError = {},
            onBackClick = {}
        )
    }
}
