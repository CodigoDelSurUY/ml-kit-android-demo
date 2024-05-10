package com.codigodelsur.mlkit.feature.translation.data.repository

import com.codigodelsur.mlkit.feature.translation.data.datasource.TranslationLocalDataSource
import com.codigodelsur.mlkit.feature.translation.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val localDataSource: TranslationLocalDataSource
) : TranslationRepository {

    override suspend fun translateToEnglish(text: String): String {
        val inputLanguage = localDataSource.identifyLanguage(text = text)
        return localDataSource.translate(
            inputLanguage = inputLanguage,
            outputLanguage = "en",
            text = text
        )
    }
}