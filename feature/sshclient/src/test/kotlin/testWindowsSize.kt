import org.junit.Test
import kotlin.test.assertEquals

class testSshWindowSize{
    @Test
    fun testSize(){
        assertEquals(
            360/15, 48
        )
    }
}