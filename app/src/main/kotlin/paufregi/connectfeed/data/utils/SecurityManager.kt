package paufregi.connectfeed.data.utils

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

object SecurityManager {
    init {
        AeadConfig.register()
    }

    fun getAead(context: Context, keyName: String): Aead {
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "${keyName}_keyset", "${keyName}_pref")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://$keyName")
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(
            RegistryConfiguration.get(),
            Aead::class.java
        )
    }
}