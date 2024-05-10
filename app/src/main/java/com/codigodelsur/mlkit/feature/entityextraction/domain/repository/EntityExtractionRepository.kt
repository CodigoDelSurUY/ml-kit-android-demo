package com.codigodelsur.mlkit.feature.entityextraction.domain.repository

import com.google.mlkit.nl.entityextraction.EntityAnnotation

interface EntityExtractionRepository {
    suspend fun extractEntities(text: String): List<EntityAnnotation>
}