import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.astune.core.ui.design.LoadingAnimation
import org.junit.Rule
import org.junit.Test

class AnimationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAnimation(){
        composeTestRule.setContent {
            LoadingAnimation(
                size = 250.dp
            )
        }
        Thread.sleep(10000)
    }
}