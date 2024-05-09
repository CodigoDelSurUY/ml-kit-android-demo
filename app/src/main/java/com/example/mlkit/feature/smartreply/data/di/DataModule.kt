package com.example.mlkit.feature.smartreply.data.di

import com.google.mlkit.nl.smartreply.SmartReply
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
    fun provideSmartReplyGenerator() = SmartReply.getClient()
}