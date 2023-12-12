package com.astune.core.network.repository

import com.astune.core.network.`object`.SshConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Session.Shell
import javax.inject.Inject

class SSHRepository @Inject constructor(
    private val client: SSHClient
) {
    suspend fun connect(host:String, port:Int, username:String, password:String): SshConnection {
        client.connect(host, port)
        client.authPassword(username, password)
        val session = client.startSession()
        session.allocateDefaultPTY()
        return SshConnection(session.startShell(), session)
    }

    suspend fun send(message:ByteArray, shell:Shell){
        withContext(Dispatchers.IO) {
            shell.outputStream.write(message)
        }
    }

    suspend fun receive(shell:Shell) = flow {
        emit(shell.inputStream.readBytes())
    }.flowOn(Dispatchers.IO)

    suspend fun disconnect(sshConnection:SshConnection) {
        sshConnection.shell.close()
        sshConnection.session.close()
        client.close()
    }

}