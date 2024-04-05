import com.astune.data.utils.getTimeBetween
import org.junit.Assert.assertEquals
import java.time.Instant
import kotlin.test.Test

class TimeTest {
    @Test
    fun testTime(){
        assertEquals(getTimeBetween(Instant.now()),"0 mins ago")
    }
}