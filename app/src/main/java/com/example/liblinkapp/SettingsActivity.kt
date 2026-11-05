// app/src/main/java/com/example/liblinkapp/SettingsActivity.kt
package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.liblinkapp.utils.LocaleHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : BaseActivity() {

    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var switchNotifications: SwitchCompat
    private lateinit var switchDueDateReminders: SwitchCompat
    private lateinit var switchLinkRequests: SwitchCompat
    private lateinit var switchBiometrics: SwitchCompat
    private lateinit var switchLargeText: SwitchCompat
    private lateinit var switchHighContrast: SwitchCompat
    private lateinit var rowLanguage: LinearLayout
    private lateinit var txtLanguageValue: TextView
    private lateinit var bottomNav: BottomNavigationView

    // Only two supported languages
    private val languageOptions = arrayOf("English", "Afrikaans")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // ── Bind views ──────────────────────────────────────────────────────────
        switchDarkMode         = findViewById(R.id.switchDarkMode)
        switchNotifications    = findViewById(R.id.switchNotifications)
        switchDueDateReminders = findViewById(R.id.switchDueDateReminders)
        switchLinkRequests     = findViewById(R.id.switchLinkRequests)
        switchBiometrics       = findViewById(R.id.switchBiometrics)
        switchLargeText        = findViewById(R.id.switchLargeText)
        switchHighContrast     = findViewById(R.id.switchHighContrast)
        rowLanguage            = findViewById(R.id.rowLanguage)
        txtLanguageValue       = findViewById(R.id.txtLanguageValue)
        bottomNav              = findViewById(R.id.bottomNav)

        // ── Load saved preferences ───────────────────────────────────────────────
        val prefs = getSharedPreferences("LibLinkSettings", MODE_PRIVATE)

        switchDarkMode.isChecked         = prefs.getBoolean("darkMode", false)
        switchNotifications.isChecked    = prefs.getBoolean("notificationsEnabled", true)
        switchDueDateReminders.isChecked = prefs.getBoolean("notifyDueDates", true)
        switchLinkRequests.isChecked     = prefs.getBoolean("notifyLinkRequests", true)
        switchLargeText.isChecked        = prefs.getBoolean("largeText", false)
        switchHighContrast.isChecked     = prefs.getBoolean("highContrast", false)
        txtLanguageValue.text            = prefs.getString("language", "English")

        // Check if biometrics is available on this device
        val biometricManager = BiometricManager.from(this)
        val biometricAvailable = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS

        switchBiometrics.isEnabled = biometricAvailable
        switchBiometrics.isChecked = if (biometricAvailable) {
            prefs.getBoolean("biometricsEnabled", false)
        } else false

        setDependentSwitchesEnabled(switchNotifications.isChecked)

        // ── Switch listeners — save every change immediately ──────────────────
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("darkMode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notificationsEnabled", isChecked).apply()
            setDependentSwitchesEnabled(isChecked)
        }

        switchDueDateReminders.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifyDueDates", isChecked).apply()
        }

        switchLinkRequests.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifyLinkRequests", isChecked).apply()
        }

        switchBiometrics.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Verify identity before enabling biometrics
                showBiometricPromptForSetup(
                    onSuccess = {
                        prefs.edit().putBoolean("biometricsEnabled", true).apply()
                        Toast.makeText(this, "Biometric login enabled", Toast.LENGTH_SHORT).show()
                    },
                    onFail = {
                        // Revert the switch if authentication failed
                        switchBiometrics.isChecked = false
                    }
                )
            } else {
                prefs.edit().putBoolean("biometricsEnabled", false).apply()
                Toast.makeText(this, "Biometric login disabled", Toast.LENGTH_SHORT).show()
            }
        }

        switchLargeText.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("largeText", isChecked).apply()
        }

        switchHighContrast.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("highContrast", isChecked).apply()
        }

        // ── Language picker ──────────────────────────────────────────────────────
        rowLanguage.setOnClickListener {
            val current = languageOptions.indexOf(txtLanguageValue.text.toString())
                .let { if (it == -1) 0 else it }

            android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose_language))
                .setSingleChoiceItems(languageOptions, current) { dialog, which ->
                    val chosen = languageOptions[which]
                    txtLanguageValue.text = chosen
                    prefs.edit().putString("language", chosen).apply()
                    Toast.makeText(
                        this,
                        "${getString(R.string.language_set_to)} $chosen",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    // Restart this activity so the locale takes effect immediately
                    recreate()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        // ── Bottom navigation ────────────────────────────────────────────────────
        // Mark Settings as selected
        bottomNav.selectedItemId = R.id.navProfile   // Settings is under Profile nav

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.navBrowse -> {
                    startActivity(Intent(this, BrowseActivity::class.java))
                    finish()
                    true
                }
                R.id.navMyBooks -> {
                    startActivity(Intent(this, MyBooksActivity::class.java))
                    finish()
                    true
                }
                R.id.navProfile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ── Biometric prompt ───────────────────────────────────────────────────────
    private fun showBiometricPromptForSetup(onSuccess: () -> Unit, onFail: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                runOnUiThread { onSuccess() }
            }

            override fun onAuthenticationFailed() {
                runOnUiThread {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    onFail()
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                runOnUiThread {
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    ) {
                        Toast.makeText(
                            this@SettingsActivity,
                            "Error: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onFail()
                }
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_prompt_title))
            .setSubtitle(getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun setDependentSwitchesEnabled(enabled: Boolean) {
        switchDueDateReminders.isEnabled = enabled
        switchLinkRequests.isEnabled = enabled
    }
}