package com.codigodelsur.mlkit.feature.entityextraction.presentation.model

import com.google.mlkit.nl.entityextraction.EntityAnnotation

data class PEntityExtractionResult(
    val text: String,
    val entityAnnotations: List<EntityAnnotation>
)