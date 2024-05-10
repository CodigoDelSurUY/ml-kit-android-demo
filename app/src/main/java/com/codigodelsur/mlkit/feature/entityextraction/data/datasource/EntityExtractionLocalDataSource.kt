package com.codigodelsur.mlkit.feature.entityextraction.data.datasource

import com.google.mlkit.nl.entityextraction.EntityAnnotation
import com.google.mlkit.nl.entityextraction.EntityExtractor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface EntityExtractionLocalDataSource {
    suspend fun extractEntities(text: String): List<EntityAnnotation>
}

class EntityExtractionLocalDataSourceImpl @Inject constructor(
    private val entityExtractor: EntityExtractor
) : EntityExtractionLocalDataSource {

    override suspend fun extractEntities(text: String): List<EntityAnnotation> =
        suspendCoroutine { continuation ->
            entityExtractor.downloadModelIfNeeded()
                .addOnFailureListener(continuation::resumeWithException)
                .addOnSuccessListener {
                    entityExtractor.annotate(text)
                        .addOnFailureListener(continuation::resumeWithException)
                        .addOnSuccessListener { continuation.resume(it) }
                }
        }
}
