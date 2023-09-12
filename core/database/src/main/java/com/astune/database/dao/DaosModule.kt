package com.astune.database.dao

import com.astune.database.MCenterDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaosModule {
    @Provides
    fun providesDeviceDao(
        database:MCenterDB
    ): DeviceDao = database.deviceDao()

    @Provides
    fun providesWebLinkDao(
        database:MCenterDB
    ): WebLinkDao = database.webLinkDao()

    @Provides
    fun providesSSHLinkDao(
        database:MCenterDB
    ): SSHLinkDao = database.sshLinkDao()
}