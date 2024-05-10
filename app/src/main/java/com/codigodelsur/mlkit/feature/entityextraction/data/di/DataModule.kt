package com.codigodelsur.mlkit.feature.entityextraction.data.di

import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
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
    fun provideEntityExtractionOptions() = EntityExtractorOptions
        .Builder(EntityExtractorOptions.ENGLISH)
        .build()

    @Provides
    @Singleton
    fun provideEntityExtractionClient(options: EntityExtractorOptions) =
        EntityExtraction.getClient(options)

}