package com.codigodelsur.mlkit.feature.translation.data.di

import com.codigodelsur.mlkit.feature.translation.data.datasource.TranslationLocalDataSource
import com.codigodelsur.mlkit.feature.translation.data.datasource.TranslationLocalDataSourceImpl
import com.codigodelsur.mlkit.feature.translation.data.repository.TranslationRepositoryImpl
import com.codigodelsur.mlkit.feature.translation.domain.repository.TranslationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractDataModule {

    @Binds
    @Singleton
    abstract fun bindTranslationLocalDataSource(
        translationLocalDataSourceImpl: TranslationLocalDataSourceImpl
    ): TranslationLocalDataSource


    @Binds
    @Singleton
    abstract fun bindTranslationRepository(
        translationRepositoryImpl: TranslationRepositoryImpl
    ): TranslationRepository
}