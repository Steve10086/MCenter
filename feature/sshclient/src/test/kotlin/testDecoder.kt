

import com.astune.sshclient.fake.getTopContent
import org.junit.Test
import kotlin.test.assertEquals

class testDecode{
    @Test
    fun testDecoder(){
        val content = getTopContent()
        content.toAnnotatedString()
        assertEquals(content.header, "user@openstick:~\$ ")
    }
}
