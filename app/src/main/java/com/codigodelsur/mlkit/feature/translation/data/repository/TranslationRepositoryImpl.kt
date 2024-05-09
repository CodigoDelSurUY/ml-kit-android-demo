package com.codigodelsur.mlkit.feature.translation.data.repository

import com.codigodelsur.mlkit.feature.translation.data.datasource.TranslationRemoteDataSource
import com.codigodelsur.mlkit.feature.translation.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val remoteDataSource: TranslationRemoteDataSource
) : TranslationRepository {

    override suspend fun translateToEnglish(text: String): String {
        val inputLanguage = remoteDataSource.identifyLanguage(text = text)
        return remoteDataSource.translate(
            inputLanguage = inputLanguage,
            outputLanguage = "en",
            text = text
        )
    }
}