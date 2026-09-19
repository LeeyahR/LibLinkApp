package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.models.LoginRequest
import com.example.liblinkapp.utils.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnSignIn: Button
    private lateinit var btnSSO: Button
    private lateinit var forgotPassword: TextView
    private lateinit var registerText: TextView

    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        api = ApiClient.create(this)
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()

            return
        }

        emailEditText =
            findViewById(R.id.emailEditText)

        passwordEditText =
            findViewById(R.id.passwordEditText)

        btnSignIn =
            findViewById(R.id.btnSignIn)

        btnSSO =
            findViewById(R.id.btnSSO)

        forgotPassword =
            findViewById(R.id.forgotPassword)

        registerText =
            findViewById(R.id.registerText)

        // -----------------------------
        // SIGN IN
        // -----------------------------

        btnSignIn.setOnClickListener {
            loginUser()
        }

        // -----------------------------
        // SSO
        // -----------------------------

        btnSSO.setOnClickListener {

            Toast.makeText(
                this,
                "University SSO login will be available when the university authentication service is connected.",
                Toast.LENGTH_LONG
            ).show()
        }

        // -----------------------------
        // FORGOT PASSWORD
        // -----------------------------

        forgotPassword.setOnClickListener {

            Toast.makeText(
                this,
                "Password reset will be handled by the API/email service.",
                Toast.LENGTH_LONG
            ).show()
        }

        // -----------------------------
        // REGISTER
        // -----------------------------

        registerText.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        // -----------------------------
        // WINDOW INSETS
        // -----------------------------

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    private fun loginUser() {

        val email =
            emailEditText.text.toString().trim()

        val password =
            passwordEditText.text.toString()

        // -----------------------------
        // VALIDATION
        // -----------------------------

        if (email.isEmpty()) {

            emailEditText.error =
                "Please enter your student email"

            emailEditText.requestFocus()

            return
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            emailEditText.error =
                "Please enter a valid email address"

            emailEditText.requestFocus()

            return
        }

        if (password.isEmpty()) {

            passwordEditText.error =
                "Please enter your password"

            passwordEditText.requestFocus()

            return
        }

        // -----------------------------
        // DISABLE BUTTON
        // -----------------------------

        btnSignIn.isEnabled = false

        // -----------------------------
        // CREATE REQUEST
        // -----------------------------

        val request =
            LoginRequest(
                email = email,
                password = password
            )

        // -----------------------------
        // CALL API
        // -----------------------------

        lifecycleScope.launch {

            try {

                val response =
                    api.login(request)

                btnSignIn.isEnabled = true

                if (response.isSuccessful) {

                    val loginResponse =
                        response.body()

                    if (loginResponse != null) {

                        // Save user session
                        sessionManager.saveSession(
                            userId = loginResponse.userId,
                            name = loginResponse.name,
                            email = loginResponse.email,
                            role = loginResponse.role
                        )

                        Toast.makeText(
                            this@MainActivity,
                            "Login successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this@MainActivity,
                                HomeActivity::class.java
                            )
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Invalid response from API",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Incorrect email or password",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                btnSignIn.isEnabled = true

                Toast.makeText(
                    this@MainActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}