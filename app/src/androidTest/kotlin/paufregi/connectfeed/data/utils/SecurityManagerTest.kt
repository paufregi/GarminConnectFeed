package paufregi.connectfeed.data.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SecurityManagerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `getAead returns an Aead that can encrypt and decrypt data`() {
        val keyName = "test_security_manager_key"
        val plaintext = "sensitive payload".toByteArray(Charsets.UTF_8)

        val aead = SecurityManager.getAead(context, keyName)
        val ciphertext = aead.encrypt(plaintext, null)

        assertThat(ciphertext).isNotEmpty()
        assertThat(ciphertext).isNotEqualTo(plaintext)
        assertThat(aead.decrypt(ciphertext, null)).isEqualTo(plaintext)
    }
}
