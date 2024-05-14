package com.codigodelsur.mlkit.feature.translation.data.datasource

import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface TranslationLocalDataSource {
    suspend fun identifyLanguage(text: String): String
    suspend fun translate(inputLanguage: String, outputLanguage: String, text: String): String
}

class TranslationLocalDataSourceImpl @Inject constructor(
    private val languageIdentifier: LanguageIdentifier
) : TranslationLocalDataSource {

    override suspend fun identifyLanguage(text: String): String = suspendCoroutine { continuation ->
        languageIdentifier.identifyLanguage(text)
            .addOnFailureListener(continuation::resumeWithException)
            .addOnSuccessListener(continuation::resume)
    }

    override suspend fun translate(
        inputLanguage: String,
        outputLanguage: String,
        text: String
    ): String = suspendCoroutine { continuation ->
        val inputLan = TranslateLanguage.fromLanguageTag(inputLanguage)
        val outputLan = TranslateLanguage.fromLanguageTag(outputLanguage)
        if (inputLan == null || outputLan == null) {
            continuation.resumeWithException(IllegalArgumentException("Unrecognized languages"))
            return@suspendCoroutine
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputLan)
            .setTargetLanguage(outputLan)
            .build()
        val translator = Translation.getClient(options)
        // TODO: You should not keep unused models in the device since they each take up to 30 MB, in this example they are kept for simplicity's sake
        translator.downloadModelIfNeeded()
            .addOnFailureListener {
                continuation.resumeWithException(it)
                translator.close()
            }
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnFailureListener(continuation::resumeWithException)
                    .addOnSuccessListener(continuation::resume)
                    .addOnCompleteListener { translator.close() }
            }

    }
}