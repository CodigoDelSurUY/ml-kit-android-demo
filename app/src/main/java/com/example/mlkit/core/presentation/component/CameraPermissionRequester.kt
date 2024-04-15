package com.example.mlkit.core.presentation.component

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.app.ui.theme.Typography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionRequester(
    modifier: Modifier,
    grantedContent: @Composable () -> Unit = {}
) {
    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    if (cameraPermission.status.isGranted) {
        grantedContent()
    } else {
        CameraPermissionNotGranted(
            modifier = modifier,
            onRequestPermission = { cameraPermission.launchPermissionRequest() }
        )
    }
}

@Composable
private fun CameraPermissionNotGranted(
    modifier: Modifier,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(50.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(R.drawable.ic_camera_24),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = stringResource(id = R.string.camera_permission_requester_title),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = stringResource(id = R.string.camera_permission_requester_message),
            textAlign = TextAlign.Center,
            style = Typography.bodyMedium
        )
        Button(onClick = onRequestPermission) {
            Text(text = stringResource(id = R.string.camera_permission_requester_button))
        }
    }
}


@Preview
@Composable
private fun CameraPermissionNotGrantedPreview() {
    MlkTheme {
        CameraPermissionNotGranted(modifier = Modifier.fillMaxSize(), onRequestPermission = {})
    }
}