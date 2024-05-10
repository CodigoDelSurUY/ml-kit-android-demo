package com.codigodelsur.mlkit.feature.entityextraction.domain.usecase

import com.codigodelsur.mlkit.feature.entityextraction.domain.repository.EntityExtractionRepository
import javax.inject.Inject

class ExtractEntitiesUseCase @Inject constructor(
    private val repository: EntityExtractionRepository
) {

    suspend operator fun invoke(text: String) = repository.extractEntities(text = text)
}