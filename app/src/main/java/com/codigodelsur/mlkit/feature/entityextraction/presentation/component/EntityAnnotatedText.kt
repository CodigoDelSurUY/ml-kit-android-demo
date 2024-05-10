package com.codigodelsur.mlkit.feature.entityextraction.presentation.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.google.mlkit.nl.entityextraction.DateTimeEntity
import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import java.net.URLEncoder

@Composable
fun EntityAnnotatedText(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    text: String,
    entityAnnotations: List<EntityAnnotation>
) {
    val tag = "ENTITY"
    val annotatedString = buildAnnotatedString {
        append(text)
        entityAnnotations.forEachIndexed { annotationIndex, annotation ->
            if (annotation.isSupported()) {
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue, textDecoration = TextDecoration.Underline
                    ), start = annotation.start, end = annotation.end
                )
                addStringAnnotation(
                    tag = tag,
                    annotation = "$annotationIndex",
                    start = annotation.start,
                    end = annotation.end
                )
            }
        }
    }

    val context = LocalContext.current
    ClickableText(modifier = modifier, style = style, text = annotatedString, onClick = { offset ->
        annotatedString.getStringAnnotations(tag, offset, offset).firstOrNull()
            ?.let { stringAnnotation ->
                val entityAnnotation = entityAnnotations[stringAnnotation.item.toInt()]
                val annotatedText = entityAnnotation.annotatedText
                val entity = entityAnnotation.entities.first()
                when {
                    entity.type == Entity.TYPE_ADDRESS -> openGoogleMaps(context, annotatedText)
                    entity.type == Entity.TYPE_PHONE -> dialNumber(context, annotatedText)
                    entity.type == Entity.TYPE_URL -> openUrl(context, annotatedText)
                    entity.type == Entity.TYPE_EMAIL -> sendEmail(context, annotatedText)
                    entity is DateTimeEntity -> openCalendar(context, entity.timestampMillis)
                }
            }
    })
}

private fun EntityAnnotation.isSupported(): Boolean {
    with(entities[0]) {
        return type == Entity.TYPE_ADDRESS ||
                type == Entity.TYPE_PHONE ||
                type == Entity.TYPE_URL ||
                type == Entity.TYPE_EMAIL ||
                this is DateTimeEntity
    }

}

private fun openUrl(context: Context, url: String) {
    val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
    openUri(context, fullUrl)
}

private fun openGoogleMaps(context: Context, address: String) {
    val encodedAddress = URLEncoder.encode(address, "UTF-8")
    openUri(context, "geo:0,0?q=$encodedAddress")
}

private fun dialNumber(context: Context, phoneNumber: String) {
    openUri(context, "tel:$phoneNumber")
}

private fun sendEmail(context: Context, email: String) {
    openUri(context, "mailto:$email")
}

private fun openCalendar(context: Context, startTime: Long) {
    val intent = Intent(Intent.ACTION_EDIT).apply {
        setDataAndType(CalendarContract.Events.CONTENT_URI, "vnd.android.cursor.item/event")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

private fun openUri(context: Context, uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}