package com.astune.database.Dao

import com.astune.database.MCenterDB
import com.astune.mcenter.`object`.Dao.DeviceDao
import com.astune.mcenter.`object`.Dao.SSHLinkDao
import com.astune.mcenter.`object`.Dao.WebLinkDao
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