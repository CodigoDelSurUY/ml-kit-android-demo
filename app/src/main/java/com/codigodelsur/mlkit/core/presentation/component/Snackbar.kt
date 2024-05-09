package com.codigodelsur.mlkit.core.presentation.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.res.stringResource
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.model.PSnackbar

val LocalSnackBarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackBarHostState provided")
}

@Composable
fun ShowSnackbarEffect(
    snackbar: PSnackbar,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onShown: () -> Unit
) {
    val snackBarHostState = LocalSnackBarHostState.current
    val message = when (snackbar) {
        is PSnackbar.Resource -> stringResource(id = snackbar.resId)
        is PSnackbar.Text -> snackbar.message ?: stringResource(id = R.string.error_default)
    }
    LaunchedEffect(Unit) {
        snackBarHostState.showSnackbar(
            message = message,
            duration = duration
        )
        onShown()
    }
}
