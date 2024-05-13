package com.codigodelsur.mlkit.feature.posedetection.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.R
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme

@Composable
fun PoseLegend(
    modifier: Modifier = Modifier,
    reps: Int,
    className: String?
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(8.dp)
            .width(200.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.pose_detection_reps_counter, reps),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        Text(
            text = stringResource(
                id = R.string.pose_detection_classification,
                className ?: stringResource(id = R.string.pose_detection_classification_none)
            ),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

@Preview
@Composable
private fun PoseLegendPreview() {
    MlkTheme {
        PoseLegend(
            reps = 10,
            className = "push_up"
        )
    }
}