
import androidx.compose.ui.test.junit4.createComposeRule
import com.astune.link.subPanels.WebLinkPage
import org.junit.Rule
import org.junit.Test

class WebPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWebPage(){
        composeTestRule.setContent {
            WebLinkPage(
                uri = "https://www.baidu.com",
                onExit = {}
            )
        }
        Thread.sleep(10000)
    }
}