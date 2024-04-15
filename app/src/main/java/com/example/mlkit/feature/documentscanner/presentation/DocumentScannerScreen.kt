package com.example.mlkit.feature.documentscanner.presentation

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.core.model.PSnackbar
import com.example.mlkit.core.presentation.component.MlkTopAppBar
import com.example.mlkit.core.presentation.component.ShowSnackbarEffect
import com.example.mlkit.core.util.findActivity
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

@Composable
fun DocumentScannerRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val activity = LocalContext.current.findActivity()
    var showSaveSuccess by remember { mutableStateOf(false) }
    var scanError by remember { mutableStateOf<String?>(null) }
    var documentScan by remember { mutableStateOf<GmsDocumentScanningResult?>(null) }
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setPageLimit(5)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            documentScan = GmsDocumentScanningResult.fromActivityResultIntent(activityResult.data)
        }
    }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        uri?.let { targetUri ->
            documentScan?.pdf?.uri?.let { sourceUri ->
                try {
                    activity.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                        activity.contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    documentScan = null
                    showSaveSuccess = true
                } catch (th: Throwable) {
                    scanError = th.message
                }
            }
        }
    }

    if (scanError != null) {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(scanError)) {
            scanError = null
        }
    }

    if (showSaveSuccess) {
        ShowSnackbarEffect(snackbar = PSnackbar.Resource(R.string.document_scanner_saved_message)) {
            showSaveSuccess = false
        }
    }

    DocumentScannerScreen(
        modifier = modifier,
        documentPages = documentScan?.pages?.map { it.imageUri } ?: emptyList(),
        onScanClick = {
            scanner.getStartScanIntent(activity)
                .addOnSuccessListener {
                    scannerLauncher
                        .launch(IntentSenderRequest.Builder(it).build())
                }
                .addOnFailureListener {
                    scanError = it.message
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
    documentPages: List<Uri>,
    onScanClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_document_scanner_title,
            onNavigationClick = onBackClick
        )
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(1.0f),
            verticalArrangement = Arrangement.Center
        ) {
            if (documentPages.isEmpty()) {
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = onScanClick
                ) {
                    Text(text = stringResource(id = R.string.document_scanner_scan_document_button))
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .weight(1.0f)
                ) {
                    items(documentPages) { page ->
                        DocumentPageItem(
                            modifier = Modifier.fillMaxWidth(),
                            page = page
                        )
                    }
                }
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    onClick = onSaveClick
                ) {
                    Text(text = stringResource(id = R.string.document_scanner_save_document_button))
                }
            }
        }
    }
}

@Composable
private fun DocumentPageItem(
    modifier: Modifier,
    page: Uri
) {
    AsyncImage(
        modifier = modifier,
        model = page,
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Preview
@Composable
private fun DocumentScannerScreenPreview() {
    MlkTheme {
        DocumentScannerScreen(
            documentPages = listOf(),
            onScanClick = {},
            onSaveClick = {},
            onBackClick = {})
    }
}