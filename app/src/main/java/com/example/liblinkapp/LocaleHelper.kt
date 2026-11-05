// app/src/main/java/com/example/liblinkapp/utils/LocaleHelper.kt
package com.example.liblinkapp.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    // Map display names to BCP-47 locale codes
    private val languageMap = mapOf(
        "English"    to "en",
        "Afrikaans"  to "af"
    )

    fun applyLanguage(context: Context, languageName: String): Context {
        val code = languageMap[languageName] ?: "en"
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun getSavedLanguage(context: Context): String {
        return context.getSharedPreferences("LibLinkSettings", Context.MODE_PRIVATE)
            .getString("language", "English") ?: "English"
    }
}