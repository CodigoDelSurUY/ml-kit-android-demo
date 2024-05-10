package com.codigodelsur.mlkit.feature.entityextraction.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.component.LoadingButton
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.component.ShowSnackbarEffect
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.feature.entityextraction.presentation.component.EntityAnnotatedText
import com.codigodelsur.mlkit.feature.entityextraction.presentation.model.PEntityExtractionResult

@Composable
fun EntityExtractionRoute(
    modifier: Modifier = Modifier,
    viewModel: EntityExtractionViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.extractionError?.let {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(it)) {
            viewModel.hideExtractionError()
        }
    }

    EntityExtractionScreen(
        modifier = modifier,
        inputText = state.inputText,
        isLoading = state.isLoading,
        extractionResult = state.extractionResult,
        onInputTextChange = viewModel::updateInputText,
        onExtractClick = viewModel::extractEntities,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntityExtractionScreen(
    modifier: Modifier = Modifier,
    inputText: String,
    extractionResult: PEntityExtractionResult?,
    isLoading: Boolean,
    onInputTextChange: (String) -> Unit,
    onExtractClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.fillMaxSize()) {
        MlkTopAppBar(
            titleRes = R.string.feature_entity_extraction_title,
            onNavigationClick = onBackClick
        )
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .weight(1.0f)
                .verticalScroll(rememberScrollState())
                .padding(all = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.entity_extraction_instructions),
                style = Typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                value = inputText,
                onValueChange = onInputTextChange,
                label = { Text(text = stringResource(id = R.string.entity_extraction_input_label)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.entity_extraction_extract_button),
                enabled = inputText.isNotBlank(),
                loading = isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onExtractClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (extractionResult != null) {
                EntityAnnotatedText(
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.bodyLarge,
                    text = extractionResult.text,
                    entityAnnotations = extractionResult.entityAnnotations
                )
            }
        }
    }
}

@Preview
@Composable
private fun EntityExtractionScreenPreview() {
    MlkTheme {
        EntityExtractionScreen(
            inputText = "",
            extractionResult = null,
            isLoading = false,
            onInputTextChange = {},
            onExtractClick = {},
            onBackClick = {}
        )
    }
}