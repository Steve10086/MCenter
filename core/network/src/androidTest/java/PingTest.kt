
import com.astune.core.network.utils.averagePing
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


class PingTest {
    @Test
    fun testPing() = runTest{
        val result = averagePing("192.168.1.114",  timeout = 10)
        assertEquals(result.toInt(),4087)
    }
}