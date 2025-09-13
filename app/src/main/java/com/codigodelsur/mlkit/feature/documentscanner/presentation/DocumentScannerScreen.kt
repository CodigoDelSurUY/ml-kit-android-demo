package com.codigodelsur.mlkit.feature.documentscanner.presentation

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.component.ShowSnackbarEffect
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.core.presentation.util.findActivity
import com.codigodelsur.mlkit.feature.documentscanner.presentation.component.DocumentPageItem
import com.codigodelsur.mlkit.feature.documentscanner.presentation.model.PScannedDocument
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

@Composable
fun DocumentScannerRoute(
    modifier: Modifier = Modifier,
    viewModel: DocumentScannerViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalContext.current.findActivity()
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val result = GmsDocumentScanningResult.fromActivityResultIntent(activityResult.data)
            viewModel.updateDocumentScan(result = result)
        }
    }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        uri?.let { targetUri ->
            state.scannedDocument?.pdf?.let { sourceUri ->
                try {
                    activity.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                        activity.contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    viewModel.showSaveSuccess()
                } catch (th: Throwable) {
                    th.message?.let {
                        viewModel.showScannerError(error = it)
                    }
                }
            }
        }
    }

    state.scannerError?.let {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(it)) {
            viewModel.hideScannerError()
        }
    }

    if (state.saveSuccess) {
        ShowSnackbarEffect(snackbar = PSnackbar.Resource(R.string.document_scanner_saved_message)) {
            viewModel.hideSaveSuccess()
        }
    }

    DocumentScannerScreen(
        modifier = modifier,
        scannedDocument = state.scannedDocument,
        onScanClick = {
            scanner.getStartScanIntent(activity)
                .addOnSuccessListener {
                    scannerLauncher
                        .launch(IntentSenderRequest.Builder(it).build())
                }
                .addOnFailureListener { failure ->
                    failure.message?.let {
                        viewModel.showScannerError(error = it)
                    }
                }
        },
        onSaveClick = {
            createDocumentLauncher.launch("document.pdf")
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DocumentScannerScreen(
    modifier: Modifier = Modifier,
    scannedDocument: PScannedDocument?,
    onScanClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        MlkTopAppBar(
            titleRes = R.string.feature_document_scanner_title,
            onNavigationClick = onBackClick
        )
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (scannedDocument == null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.document_scanner_instructions),
                    style = Typography.bodyMedium
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onScanClick
                ) {
                    Text(text = stringResource(id = R.string.document_scanner_scan_document_button))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(scannedDocument.pages) { page ->
                        DocumentPageItem(
                            modifier = Modifier.fillMaxWidth(),
                            page = page
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSaveClick
                ) {
                    Text(text = stringResource(id = R.string.document_scanner_save_document_button))
                }
            }
        }
    }
}

@Preview
@Composable
private fun DocumentScannerScreenPreview() {
    MlkTheme {
        DocumentScannerScreen(
            scannedDocument = null,
            onScanClick = {},
            onSaveClick = {},
            onBackClick = {})
    }
}