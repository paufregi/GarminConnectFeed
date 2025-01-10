package paufregi.connectfeed.data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CryptoTest {

    @Test
    fun `Encrypt and decrypt data`() {
        val data = "ConnectFeed".toByteArray()
        val encrypted = Crypto.encrypt(data)
        val decrypted = Crypto.decrypt(encrypted)
        assertThat(decrypted).isEqualTo(data)
    }
}