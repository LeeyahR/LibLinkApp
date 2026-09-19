package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchDarkMode: Switch
    private lateinit var switchNotifications: Switch
    private lateinit var switchDueDateReminders: Switch
    private lateinit var switchLinkRequests: Switch
    private lateinit var switchLargeText: Switch
    private lateinit var switchHighContrast: Switch

    private lateinit var rowLanguage: LinearLayout
    private lateinit var txtLanguageValue: TextView

    private lateinit var navHome: TextView
    private lateinit var navBrowse: TextView
    private lateinit var navMyBooks: TextView
    private lateinit var navProfile: TextView

    private val languageOptions = arrayOf("English", "Afrikaans", "isiZulu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        switchDarkMode = findViewById(R.id.switchDarkMode)
        switchNotifications = findViewById(R.id.switchNotifications)
        switchDueDateReminders = findViewById(R.id.switchDueDateReminders)
        switchLinkRequests = findViewById(R.id.switchLinkRequests)
        switchLargeText = findViewById(R.id.switchLargeText)
        switchHighContrast = findViewById(R.id.switchHighContrast)

        rowLanguage = findViewById(R.id.rowLanguage)
        txtLanguageValue = findViewById(R.id.txtLanguageValue)

        navHome = findViewById(R.id.navHome)
        navBrowse = findViewById(R.id.navBrowse)
        navMyBooks = findViewById(R.id.navMyBooks)
        navProfile = findViewById(R.id.navProfile)

        // Separate prefs file, kept independent of SessionManager's LibLinkSession
        val preferences = getSharedPreferences("LibLinkSettings", MODE_PRIVATE)

        switchDarkMode.isChecked = preferences.getBoolean("darkMode", false)
        switchNotifications.isChecked = preferences.getBoolean("notificationsEnabled", true)
        switchDueDateReminders.isChecked = preferences.getBoolean("notifyDueDates", true)
        switchLinkRequests.isChecked = preferences.getBoolean("notifyLinkRequests", true)
        switchLargeText.isChecked = preferences.getBoolean("largeText", false)
        switchHighContrast.isChecked = preferences.getBoolean("highContrast", false)
        txtLanguageValue.text = preferences.getString("language", "English")

        setDependentSwitchesEnabled(switchNotifications.isChecked)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("darkMode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("notificationsEnabled", isChecked).apply()
            setDependentSwitchesEnabled(isChecked)
        }

        switchDueDateReminders.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("notifyDueDates", isChecked).apply()
        }

        switchLinkRequests.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("notifyLinkRequests", isChecked).apply()
        }

        switchLargeText.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("largeText", isChecked).apply()
        }

        switchHighContrast.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("highContrast", isChecked).apply()
        }

        rowLanguage.setOnClickListener {
            val current = languageOptions.indexOf(txtLanguageValue.text.toString())
                .let { if (it == -1) 0 else it }

            android.app.AlertDialog.Builder(this)
                .setTitle("Choose language")
                .setSingleChoiceItems(languageOptions, current) { dialog, which ->
                    val chosen = languageOptions[which]
                    txtLanguageValue.text = chosen
                    preferences.edit().putString("language", chosen).apply()
                    Toast.makeText(this, "Language set to $chosen", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Bottom nav
        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        navBrowse.setOnClickListener {
            startActivity(Intent(this, BrowseActivity::class.java))
            finish()
        }

        navMyBooks.setOnClickListener {
            startActivity(Intent(this, MyBooksActivity::class.java))
            finish()
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setDependentSwitchesEnabled(enabled: Boolean) {
        switchDueDateReminders.isEnabled = enabled
        switchLinkRequests.isEnabled = enabled
    }
}