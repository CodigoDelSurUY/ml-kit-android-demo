package com.codigodelsur.mlkit.feature.smartreply.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.core.presentation.theme.Gray700
import com.codigodelsur.mlkit.core.presentation.theme.Indigo500
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme
import com.codigodelsur.mlkit.core.presentation.theme.Typography
import com.codigodelsur.mlkit.feature.smartreply.presentation.model.PChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessageItem(
    modifier: Modifier,
    message: PChatMessage
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm", Locale.US) }
    with(message) {
        Box(
            modifier = modifier
                .padding(
                    start = if (isFromMe) 40.dp else 0.dp,
                    end = if (isFromMe) 0.dp else 40.dp
                ),
        ) {
            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = if (isFromMe) 48f else 0f,
                            bottomEnd = if (isFromMe) 0f else 48f
                        )
                    )
                    .background(if (isFromMe) Indigo500 else Gray700)
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp, bottom = 8.dp)
                    .align(if (isFromMe) Alignment.CenterEnd else Alignment.CenterStart),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    style = Typography.bodyMedium,
                    color = Color.White,
                    text = text
                )
                Text(
                    style = Typography.labelSmall,
                    color = Color.LightGray,
                    text = dateFormat.format(date)
                )
            }
        }
    }
}


class IsFromMeProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview
@Composable
private fun ChatMessageItemPreview(
    @PreviewParameter(IsFromMeProvider::class) isFromMe: Boolean
) {
    MlkTheme {
        ChatMessageItem(
            modifier = Modifier.fillMaxWidth(),
            message = PChatMessage(
                text = "This is a message",
                isFromMe = isFromMe,
                date = Date()
            )
        )
    }
}