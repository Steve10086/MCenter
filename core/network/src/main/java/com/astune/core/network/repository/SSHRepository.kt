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
import net.schmizz.sshj.connection.channel.direct.PTYMode
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
    suspend fun connect(host:String, port:Int, username:String, password:String, width:Int?, height:Int?, col:Int?, row:Int?): SshConnection? {
        try{
            client.addHostKeyVerifier(PromiscuousVerifier())
            client.connect(host, port)
            client.authPassword(username, password)
            val session = client.startSession()
            /*default with customized size*/
            session.allocatePTY("vt100", col?:80, row?:24, width?:0, height?:0, emptyMap<PTYMode, Int>())
            return SshConnection(session.startShell(), session)
        }catch (e:Exception){
            client.close()
        }
        return null
    }

    /**
     * return true if the shell is still open
     * */
    suspend fun send(message:ByteArray, shell:Shell) = if (shell.isOpen){
        withContext(Dispatchers.IO) {
            shell.outputStream.write(message)
            shell.outputStream.flush()
            return@withContext true
        }
    } else {
        false
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