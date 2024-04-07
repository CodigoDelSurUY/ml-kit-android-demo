package com.example.mlkit.feature.documentscanner

import android.app.Activity.RESULT_OK
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import coil.compose.AsyncImage
import com.example.mlkit.R
import com.example.mlkit.app.ui.theme.MLKitTheme
import com.example.mlkit.core.util.findActivity
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.io.FileOutputStream

@Composable
fun DocumentScannerRoute(
    modifier: Modifier = Modifier,
) {

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

    val activity = LocalContext.current.findActivity()
    DocumentScannerScreen(
        modifier = modifier,
        documentPages =  documentScan?.pages?.map { it.imageUri } ?: emptyList(),
        onScanClick = {
            scanner.getStartScanIntent(activity)
                .addOnSuccessListener {
                    scannerLauncher
                        .launch(IntentSenderRequest.Builder(it).build())
                }
                .addOnFailureListener {
                    Log.e("TESt", "Error", it)
                }
        },
        onSaveClick = {
            documentScan?.pdf?.let { pdf ->
                val fos = FileOutputStream(File(activity.filesDir, "document.pdf"))
                activity.contentResolver.openInputStream(pdf.uri)?.use {
                    it.copyTo(fos)
                }
            }
        }
    )
}

@Composable
private fun DocumentScannerScreen(
    modifier: Modifier = Modifier,
    documentPages: List<Uri>,
    onScanClick: () -> Unit,
    onSaveClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onScanClick
        ) {
            Text(text = stringResource(id = R.string.document_scanner_scan_document_button))
        }

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

        if (documentPages.isNotEmpty()) {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = onSaveClick
            ) {
                Text(text = stringResource(id = R.string.document_scanner_save_document_button))
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
    MLKitTheme {
        DocumentScannerScreen(documentPages = listOf(), onScanClick = {}, onSaveClick = {})
    }
}