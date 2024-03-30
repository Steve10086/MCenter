

import com.astune.sshclient.fake.getTopContent
import org.junit.Test

class testDecode{
    @Test
    fun testDecoder(){
        val content = getTopContent()
        content.toAnnotatedString()
    }
}
