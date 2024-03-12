package com.astune.core.network.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.schmizz.sshj.SSHClient


@Module
@InstallIn(SingletonComponent::class)
object SSHModule {
    @Provides
    fun providesSshClient():SSHClient {
        return SSHClient()
    }
}