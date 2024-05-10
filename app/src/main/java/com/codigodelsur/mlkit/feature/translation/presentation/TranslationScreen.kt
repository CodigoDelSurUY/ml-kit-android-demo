package com.codigodelsur.mlkit.feature.translation.presentation

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
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.core.presentation.component.LoadingButton
import com.codigodelsur.mlkit.core.presentation.component.MlkTopAppBar
import com.codigodelsur.mlkit.core.presentation.component.ShowSnackbarEffect
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar

@Composable
fun TranslationRoute(
    modifier: Modifier = Modifier,
    viewModel: TranslationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.translationError?.let {
        ShowSnackbarEffect(snackbar = PSnackbar.Text(it)) {
            viewModel.hideTranslationError()
        }
    }

    TranslationScreen(
        modifier = modifier,
        inputText = state.inputText,
        outputText = state.outputText,
        isLoading = state.isLoading,
        onInputTextChange = { viewModel.updateInputText(text = it) },
        onTranslateClick = { viewModel.translate() },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TranslationScreen(
    modifier: Modifier,
    inputText: String,
    outputText: String,
    isLoading: Boolean,
    onInputTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MlkTopAppBar(
            titleRes = R.string.feature_translation_title,
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
                text = stringResource(id = R.string.translation_instructions),
                style = Typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                value = inputText,
                onValueChange = onInputTextChange,
                label = { Text(text = stringResource(id = R.string.translation_input_label)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.translation_translate_button),
                enabled = inputText.isNotBlank(),
                loading = isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onTranslateClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = outputText,
                style = Typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
private fun TranslationScreenPreview() {
    MlkTheme {
        TranslationScreen(
            modifier = Modifier.fillMaxSize(),
            inputText = "¡Hola!, ¿Cómo estás?",
            outputText = "Hi, how are you?",
            isLoading = false,
            onInputTextChange = {},
            onTranslateClick = {},
            onBackClick = {}
        )
    }
}