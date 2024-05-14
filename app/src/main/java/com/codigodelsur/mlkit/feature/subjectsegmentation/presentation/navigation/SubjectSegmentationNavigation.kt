package com.codigodelsur.mlkit.feature.subjectsegmentation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codigodelsur.mlkit.feature.subjectsegmentation.presentation.SubjectSegmentationRoute

const val SUBJECT_SEGMENTATION_ROUTE = "subject_segmentation_route"

fun NavController.navigateToSubjectSegmentation(navOptions: NavOptions? = null) =
    navigate(SUBJECT_SEGMENTATION_ROUTE, navOptions)

fun NavGraphBuilder.subjectSegmentationScreen(
    onBackClick: () -> Unit
) {
    composable(route = SUBJECT_SEGMENTATION_ROUTE) {
        SubjectSegmentationRoute(
            onBackClick = onBackClick,
        )
    }
}