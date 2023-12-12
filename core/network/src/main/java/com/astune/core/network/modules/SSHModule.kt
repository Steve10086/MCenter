package com.astune.core.network.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.schmizz.sshj.SSHClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SSHModule {
    @Provides
    @Singleton
    fun providesSshClient():SSHClient {
        return SSHClient()
    }
}