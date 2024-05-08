package com.example.mlkit.core.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.mlkit.app.ui.theme.MlkTheme

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            if (!loading) {
                onClick()
            }
        }
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text = text)
        }
    }
}


class IsLoadingParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview
@Composable
private fun LoadingButtonPreview(
    @PreviewParameter(IsLoadingParameterProvider::class) isLoading: Boolean
) {
    MlkTheme {
        LoadingButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Preview",
            loading = isLoading,
            onClick = {}
        )
    }
}
