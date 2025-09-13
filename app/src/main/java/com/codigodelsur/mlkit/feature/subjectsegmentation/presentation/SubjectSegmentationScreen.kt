package com.codigodelsur.mlkit.feature.subjectsegmentation.presentation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.CameraPermissionRequester
import com.codigodelsur.mlkit.core.presentation.component.MlkCameraPreview
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.component.ShowSnackbarEffect
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar
import com.codigodelsur.mlkit.feature.subjectsegmentation.presentation.component.SubjectsPhotoEditor


@Composable
fun SubjectSegmentationRoute(
    modifier: Modifier = Modifier,
    viewModel: SubjectSegmentationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.editorError?.let {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(it)) {
            viewModel.hideEditorError()
        }
    }

    SubjectSegmentationScreen(
        modifier = modifier,
        onEditorError = viewModel::showEditorError,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectSegmentationScreen(
    modifier: Modifier = Modifier,
    onEditorError: (Throwable) -> Unit,
    onBackClick: () -> Unit
) {
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var takenPhoto by remember { mutableStateOf<Bitmap?>(null) }

    BackHandler(enabled = takenPhoto != null) {
        takenPhoto = null
    }

    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_subject_segmentation_title,
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
                    cameraSelector = cameraSelector,
                    onSwitchCamera = { cameraSelector = it },
                    onPhotoCapture = { takenPhoto = it }
                )
            } else {
                SubjectsPhotoEditor(
                    modifier = Modifier.fillMaxSize(),
                    photo = takenPhoto!!,
                    onCloseClick = { takenPhoto = null },
                    onEditionError = onEditorError
                )
            }
        }
    }
}
