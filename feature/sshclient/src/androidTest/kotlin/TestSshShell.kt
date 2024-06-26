
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.astune.core.network.repository.SSHRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.schmizz.sshj.SSHClient
import org.junit.Rule
import org.junit.Test

class TestSshShell {
    @get:Rule
    val composeTestRule = createComposeRule()



    @Test
    fun testSshShell() = runBlocking{
        val sshRepository = SSHRepository(SSHClient())
        val connection = sshRepository.connect("192.168.1.116", 22, "user", "1")
        var stopSign = false

        composeTestRule.setContent {
            TestShellPage(
                onButtonClicked = {
                    launch {
                        for(c in (it + "\n")){
                            connection?.let { it1 -> sshRepository.send(c.code.toByte(), it1.shell) }
                        }
                    } },
                onExit = {
                    launch {
                        connection?.let { sshRepository.disconnect(it) }
                    }
                    stopSign = true
                })

        }

        connection?.let {
            sshRepository.receive(it.shell).collect(){
                Log.d("Test", it)
            }
        }

        while(!stopSign){
            Thread.sleep(5000)
        }

        connection?.let { sshRepository.disconnect(it) }
        return@runBlocking
    }
}

@Composable
fun TestShellPage(onButtonClicked:(String) -> Unit = {}, onExit: () -> Unit){

    var textContent by remember { mutableStateOf("") }

    Surface {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            TextField(
                modifier = Modifier.onKeyEvent {
                    Log.d("SSHtest", "triggred")
                    true
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                value = textContent,
                onValueChange = {textContent = it}
            )
            Button(
                content = {
                          Text(
                              text = "send!"
                          )
                },
                onClick = {
                    onButtonClicked(textContent)
                    textContent = ""
                }
            )
            Button(
                content = {
                    Text(
                        text = "Exit"
                    )
                },
                onClick = onExit
            )
        }

    }
}

@Preview
@Composable
fun previewTestShellPage(){
    TestShellPage {  }
}