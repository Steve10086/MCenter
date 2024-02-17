package com.astune.core.network.repository

import android.util.Log
import com.astune.core.network.`object`.SshConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.connection.channel.direct.Session.Shell
import net.schmizz.sshj.connection.channel.direct.Signal
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.concurrent.thread

class SSHRepository @Inject constructor(
    private val client: SSHClient
) {
    suspend fun connect(host:String, port:Int, username:String, password:String): SshConnection {
        client.addHostKeyVerifier(PromiscuousVerifier())
        client.connect(host, port)
        client.authPassword(username, password)
        val session = client.startSession()
        session.allocateDefaultPTY()
        return SshConnection(session.startShell(), session)
    }

    suspend fun send(message:ByteArray, shell:Shell){
        withContext(Dispatchers.IO) {
            shell.outputStream.write(message)
            shell.outputStream.flush()
        }
    }

    suspend fun receive(shell:Shell): Flow<String> {
        val content = BufferedReader(InputStreamReader(shell.inputStream))

        val buffer = CharArray(1024) // 创建一个字符数组
        return flow {
            var len = content.read(buffer)
            while (len != -1) {
                emit(String(buffer, 0, len))
                len = content.read(buffer)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun disconnect(sshConnection:SshConnection) {
        sshConnection.shell.close()
        sshConnection.session.close()
        client.close()
    }

    fun executeShellCommand(host: String, username: String, password: String, command: String) {
        val ssh = SSHClient()
        ssh.addHostKeyVerifier(PromiscuousVerifier())

        try {
            ssh.connect(host)
            ssh.authPassword(username, password)

            val session: Session = ssh.startSession()
            try {
                val shell = session.startShell()
                val output = shell.outputStream
                val input = BufferedReader(InputStreamReader(shell.inputStream))

                // Start a new thread to read the input stream
                val readerThread = thread(start = true) {
                    var line: String? = input.readLine()
                    while (line != null) {
                        Log.d("Test", line)
                        line = input.readLine()
                    }
                }

                // Write the command to the output stream
                output.write((command + "\n").toByteArray())
                output.flush()
                thread(true) {
                    Thread.sleep(10000)
                    shell.signal(Signal.INT)
                }
                // Wait for the reader thread to finish
                readerThread.join()

            } finally {
                session.close()
            }
        } finally {
            ssh.disconnect()
        }
    }

}