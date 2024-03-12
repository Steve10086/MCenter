
import com.astune.data.utils.decode
import com.astune.model.ShellContent
import org.junit.Test
import kotlin.test.assertEquals

class TestAnsiDecoder {
    @Test
    fun testDecoder(){
        val content = ShellContent()
        val ansiStr = "\u001B[H\u001B[J\u001B[38;2;25;10;74m\u000Ftop - 01:24:56 up 2 days,  3:40,  \n1 user,  load average: 0.00, 0.03, 0.01\u001B[m\u000F\u001B[m\u000F\u001B[K"
        val ansiStr2 = "SMP PREEMPT Sun Feb 6 22:10:37 CST 2022 aarch64\n" +
                "\n" +
                "The programs included with the Debian GNU/Linux system are free software;\n" +
                "the exact distribution terms for each program are described in the\n" +
                "individual files in /usr/share/doc/*/copyright.\n" +
                "\n" +
                "Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent\n" +
                "permitted by applicable law.\n" +
                "Last login: Thu Feb 15 00:01:14 2024 from 172.26.131.104\n" +
                "\u001B[?2004huser@openstick:~\$ \n" +
                "vim NEW FILE\n" +
                "\u001B[?2004l\n" +
                "files to edit\n" +
                "\u001B[?1h\u001B=\n" +
                "\u001B[1;24r\u001B[m\u001B[m\u001B[0m\u001B[H\u001B[J\u001B[24;1H\"NEW\" [New]\n" +
                "\u001B[2;1H\u001B[1m~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\u001B[0m"//\u001B[24;63H0,0-1\u001B[9CAll\u001B[1;1H"
        decode(content, text = ansiStr)
        assertEquals(
            content.content[1].text,"\u000Ftop - 01:24:56 up 2 days,  3:40,  "
        )
    }
}