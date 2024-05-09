package com.example.mlkit.feature.smartreply.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.mlkit.feature.smartreply.presentation.SmartReplyRoute

const val SMART_REPLY_ROUTE = "smart_reply_route"

fun NavController.navigateToSmartReply(navOptions: NavOptions? = null) =
    navigate(SMART_REPLY_ROUTE, navOptions)

fun NavGraphBuilder.smartReplyScreen(
    onBackClick: () -> Unit
) {
    composable(route = SMART_REPLY_ROUTE) {
        SmartReplyRoute(
            onBackClick = onBackClick,
        )
    }
}