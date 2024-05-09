package com.codigodelsur.mlkit.feature.smartreply.data.di

import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatLocalDataSource
import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatLocalDataSourceImpl
import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatRemoteDataSource
import com.codigodelsur.mlkit.feature.smartreply.data.datasource.ChatRemoteDataSourceImpl
import com.codigodelsur.mlkit.feature.smartreply.data.repository.ChatRepositoryImpl
import com.codigodelsur.mlkit.feature.smartreply.domain.repository.ChatRepository
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
    abstract fun bindChatLocalDataSourceSource(
        chatLocalDataSourceImpl: ChatLocalDataSourceImpl
    ): ChatLocalDataSource

    @Binds
    @Singleton
    abstract fun bindChatRemoteDataSourceSource(
        chatRemoteDataSourceImpl: ChatRemoteDataSourceImpl
    ): ChatRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}