

import com.astune.sshclient.fake.getContent
import org.junit.Test

class testDecode{
    @Test
    fun testDecoder(){
        val content = getContent()
        content.toAnnotatedString()
    }
}
