package com.codigodelsur.mlkit.feature.smartreply.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    inputMessage: String,
    onInputChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1.0f),
            value = inputMessage,
            onValueChange = onInputChanged,
            placeholder = {
                Text(text = "Type something")
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = onSendClicked
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_send_24),
                contentDescription = stringResource(id = R.string.smart_reply_send_button)
            )
        }
    }
}

@Preview
@Composable
private fun ChatInputPreview() {
    MlkTheme {
        ChatInput(inputMessage = "", onInputChanged = {}, onSendClicked = {})
    }
}