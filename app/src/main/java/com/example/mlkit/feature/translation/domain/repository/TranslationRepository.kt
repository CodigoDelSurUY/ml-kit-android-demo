package com.example.mlkit.feature.translation.domain.repository

interface TranslationRepository {
    suspend fun translateToEnglish(text: String): String
}