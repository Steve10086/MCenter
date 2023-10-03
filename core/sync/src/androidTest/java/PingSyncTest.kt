
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.astune.core.sync.PingSynchronizer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@HiltAndroidTest
class PingSyncTest {


    @get:Rule
    val rule = HiltAndroidRule(this)

    private val context: Context get() = InstrumentationRegistry.getInstrumentation().context


    @Before
    fun setup (){
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun test (){
        val request = OneTimeWorkRequestBuilder<PingSynchronizer>(
        ).setInputData(workDataOf(
            "ip" to arrayOf("127.0.0.1")
        ))
            .build()
        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)!!


        // Get WorkInfo and outputData
        val preRunWorkInfo = workManager.getWorkInfoById(request.id).get()

        // Assert
        assertEquals(WorkInfo.State.ENQUEUED, preRunWorkInfo.state)

        // Tells the testing framework that the constraints have been met
        testDriver.setAllConstraintsMet(request.id)

        val postRequirementWorkInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.RUNNING, postRequirementWorkInfo.state)
    }
}