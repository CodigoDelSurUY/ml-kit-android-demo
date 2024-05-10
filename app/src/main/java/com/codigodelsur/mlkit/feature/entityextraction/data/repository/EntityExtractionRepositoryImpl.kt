package com.codigodelsur.mlkit.feature.entityextraction.data.repository

import com.codigodelsur.mlkit.feature.entityextraction.data.datasource.EntityExtractionLocalDataSource
import com.codigodelsur.mlkit.feature.entityextraction.domain.repository.EntityExtractionRepository
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import javax.inject.Inject

class EntityExtractionRepositoryImpl @Inject constructor(
    private val localDataSource: EntityExtractionLocalDataSource
) : EntityExtractionRepository {

    override suspend fun extractEntities(text: String): List<EntityAnnotation> =
        localDataSource.extractEntities(text = text)

}