// app/src/main/java/com/example/liblinkapp/BaseActivity.kt
package com.example.liblinkapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.liblinkapp.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleHelper.getSavedLanguage(newBase)
        val localizedContext = LocaleHelper.applyLanguage(newBase, language)
        super.attachBaseContext(localizedContext)
    }
}