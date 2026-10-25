package paufregi.connectfeed.data.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecurityManagerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `Encrypt and decrypt data`() {
        val keyName = "test_security_manager_key"
        val plaintext = "sensitive payload".toByteArray(Charsets.UTF_8)

        val aead = SecurityManager.getAead(context, keyName)
        val ciphertext = aead.encrypt(plaintext, null)

        assertThat(ciphertext).isNotEmpty()
        assertThat(ciphertext).isNotEqualTo(plaintext)
        assertThat(aead.decrypt(ciphertext, null)).isEqualTo(plaintext)
    }
}
