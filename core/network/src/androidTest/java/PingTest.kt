
import com.astune.core.network.utils.ping
import org.junit.Test
import kotlin.test.assertEquals


class PingTest {
    @Test
    fun testPing() {
        val result = ping("127.0.0.1")
        assertEquals(result,"64 bytes from")
    }
}