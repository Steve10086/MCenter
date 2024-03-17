package com.astune.core.network.repository

import android.util.Log
import android.util.Size
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
    suspend fun connect(host:String, port:Int, username:String, password:String): SshConnection? {
        try{
            client.addHostKeyVerifier(PromiscuousVerifier())
            client.connect(host, port)
            client.authPassword(username, password)
            val session = client.startSession()
            session.allocateDefaultPTY()
            return SshConnection(session.startShell(), session)
        }catch (e:Exception){
            client.close()
        }
        return null
    }

    suspend fun send(message:Byte, shell:Shell): Boolean{
        if (shell.isOpen){
            return withContext(Dispatchers.IO) {
                shell.outputStream.write(message.toInt())
                shell.outputStream.flush()
                return@withContext true
            }
        } else {
            return false
        }
    }

    suspend fun receive(shell:Shell): Flow<String> {
        val content = BufferedReader(InputStreamReader(shell.inputStream))
        val buffer = CharArray(1024)
        return flow {
            var len = content.read(buffer)
            while (len != -1) {
                emit(String(buffer, 0, len))
                len = content.read(buffer)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun disconnect(sshConnection:SshConnection) {
        sshConnection.shell.close()
        sshConnection.session.close()
        client.close()
    }

    fun changeWindowsSize(sshConnection:SshConnection, size: Size){
        sshConnection.shell.changeWindowDimensions(size.width, size.height, 1080, 2010)
    }

    fun executeShellCommand(host: String, username: String, password: String, command: String) {
        val ssh = SSHClient()
        ssh.addHostKeyVerifier(PromiscuousVerifier())

        try {
            ssh.connect(host)
            ssh.authPassword(username, password)

            val session: Session = ssh.startSession()
            session.use { ses ->
                val shell = ses.startShell()
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
            }
        } finally {
            ssh.disconnect()
        }
    }

}