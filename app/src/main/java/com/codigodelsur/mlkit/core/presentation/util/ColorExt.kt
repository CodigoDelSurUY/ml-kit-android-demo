package com.codigodelsur.mlkit.core.presentation.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun Color.Companion.getForId(id: Int): Color {
    val random = Random(id.hashCode())
    val r = random.nextInt(256)
    val g = random.nextInt(256)
    val b = random.nextInt(256)
    return Color(r, g, b)
}