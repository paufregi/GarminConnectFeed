package paufregi.connectfeed.presentation.syncweight

/*
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.consumer
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.presentation.syncweight.SyncWeightActivity
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.stravaToken
import paufregi.connectfeed.user
import java.io.File

@HiltAndroidTest
@ExperimentalMaterial3Api
@RunWith(AndroidJUnit4::class)
class SyncWeightActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var authStore: AuthStore

    @Inject
    lateinit var stravaStore: StravaStore

    @Inject
    lateinit var instrumentationContext: Context

    @JvmField @Rule val connectServer = MockWebServerRule(connectPort, sslSocketFactory, connectDispatcher)
    @JvmField @Rule val strava = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()

        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
    }

    // FIXME: android.os.FileUriExposedException: file:///data/user/0/paufregi.connectfeed/files/test.csv exposed beyond app through Intent.getData()
    @Test
    fun `Sync weight`() = runTest {
        authStore.saveUser(user)
        authStore.saveConsumer(consumer)
        authStore.savePreAuthToken(preAuthToken)
        authStore.saveAuthToken(authToken)
        stravaStore.saveToken(stravaToken)

        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val testFile = File("${instrumentationContext.filesDir}/test.csv")
        testFile.writeText(csvText)

        val intent = Intent(Intent.ACTION_SEND).apply {
            setDataAndType(Uri.fromFile(testFile), "application/vnd.ms-excel")
        }
        ActivityScenario.launchActivityForResult<SyncWeightActivity>(intent)

        composeTestRule.onNodeWithText("Sync succeeded").assertIsDisplayed()
    }
}
*/