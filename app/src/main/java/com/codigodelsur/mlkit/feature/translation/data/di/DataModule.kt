package com.codigodelsur.mlkit.feature.translation.data.di

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideLanguageIdentificationOptions() = LanguageIdentificationOptions.Builder()
        .build()

    @Provides
    @Singleton
    fun provideLanguageIdentificationClient(options: LanguageIdentificationOptions) =
        LanguageIdentification.getClient(options)

}