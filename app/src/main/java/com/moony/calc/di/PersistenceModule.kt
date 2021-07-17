package com.moony.calc.di

import android.app.Application
import com.moony.calc.database.MoonyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): MoonyDatabase =
        MoonyDatabase.getInstance(application)

    @Provides
    @Singleton
    fun provideTransactionDao(database: MoonyDatabase) = database.getTransactionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: MoonyDatabase) = database.getCategoryDao()
}