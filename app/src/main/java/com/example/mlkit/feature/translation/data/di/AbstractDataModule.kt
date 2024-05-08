package com.example.mlkit.feature.translation.data.di

import com.example.mlkit.feature.translation.data.datasource.TranslationRemoteDataSource
import com.example.mlkit.feature.translation.data.datasource.TranslationRemoteDataSourceImpl
import com.example.mlkit.feature.translation.data.repository.TranslationRepositoryImpl
import com.example.mlkit.feature.translation.domain.repository.TranslationRepository
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
    abstract fun bindTranslationRemoteDataSource(
        translationRemoteDataSourceImpl: TranslationRemoteDataSourceImpl
    ): TranslationRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindTranslationRepository(
        translationRepositoryImpl: TranslationRepositoryImpl
    ): TranslationRepository
}