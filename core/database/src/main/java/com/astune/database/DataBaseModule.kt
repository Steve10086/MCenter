package com.astune.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun providesMCDatabase(
        @ApplicationContext context: Context
    ):MCenterDB = Room.databaseBuilder(
        context,
        MCenterDB::class.java,
        "MCenterDB"
    ).build()
}