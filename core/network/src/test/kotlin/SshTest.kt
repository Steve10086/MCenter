import net.schmizz.sshj.SSHClient
import org.junit.Test
import kotlin.test.assertEquals

class SshTest {
    @Test
    fun testClient(){
        val client = SSHClient()
        assertEquals(client.isConnected, false)
    }
}