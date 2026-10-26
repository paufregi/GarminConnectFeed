package paufregi.connectfeed.data.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.FakeAndroidKeyStore

@RunWith(AndroidJUnit4::class)
class CryptoTest {

    @Before
    fun setup() {
        FakeAndroidKeyStore.install()
    }

    @Test
    fun `Encrypt and decrypt data`() {
        val data = "ConnectFeed"
        val encrypted = Crypto.encrypt(data)
        val decrypted = Crypto.decrypt(encrypted)

        assertThat(encrypted).isNotEqualTo(data.toByteArray())
        assertThat(decrypted).isEqualTo(data)
    }

    @Test
    fun `Encrypting twice produces different ciphertext`() {
        val data = "ConnectFeed"
        val first = Crypto.encrypt(data)
        val second = Crypto.encrypt(data)

        assertThat(first).isNotEqualTo(second)
        assertThat(Crypto.decrypt(first)).isEqualTo(data)
        assertThat(Crypto.decrypt(second)).isEqualTo(data)
    }
}

