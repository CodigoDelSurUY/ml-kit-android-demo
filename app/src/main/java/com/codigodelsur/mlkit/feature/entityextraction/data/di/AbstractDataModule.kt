package com.codigodelsur.mlkit.feature.entityextraction.data.di

import com.codigodelsur.mlkit.feature.entityextraction.data.datasource.EntityExtractionLocalDataSource
import com.codigodelsur.mlkit.feature.entityextraction.data.datasource.EntityExtractionLocalDataSourceImpl
import com.codigodelsur.mlkit.feature.entityextraction.data.repository.EntityExtractionRepositoryImpl
import com.codigodelsur.mlkit.feature.entityextraction.domain.repository.EntityExtractionRepository
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
    abstract fun bindEntityExtractionLocalDataSource(
        entityExtractionLocalDataSourceImpl: EntityExtractionLocalDataSourceImpl
    ): EntityExtractionLocalDataSource


    @Binds
    @Singleton
    abstract fun bindEntityExtractionRepository(
        entityExtractionRepositoryImpl: EntityExtractionRepositoryImpl
    ): EntityExtractionRepository
}