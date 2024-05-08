package com.example.mlkit.feature.translation.domain.usecase

import com.example.mlkit.feature.translation.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslateToEnglishUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(text: String): String = repository.translateToEnglish(text = text)
}