
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import org.junit.Test
import kotlin.test.assertEquals

class testKeyCode {
    @Test
    fun testCode(){
        assertEquals(
            Key(74).nativeKeyCode.toChar(),
            'a'
        )
    }
}