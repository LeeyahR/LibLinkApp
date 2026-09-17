package com.example.liblinkapp

import android.content.Intent
import android.inputmethodservice.ExtractEditText
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText : EditText
    private lateinit var btnSignIn : Button
    private lateinit var btnSSO : Button
    private lateinit var forgotPassword : TextView
    private lateinit var registerText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnSSO = findViewById(R.id.btnSSO)
        forgotPassword = findViewById(R.id.forgotPassword)
        registerText = findViewById(R.id.registerText)

        //sign in
        btnSignIn.setOnClickListener {
            loginUser()
        }

        //SSO Sign in
        btnSSO.setOnClickListener {
            Toast.makeText(this, "University SSO login will be available when the university authentication service is connected.", Toast.LENGTH_LONG).show()
        }

        //Forgot password
        forgotPassword.setOnClickListener {

            val enteredEmail =
                emailEditText.text.toString().trim()

            val preferences =
                getSharedPreferences(
                    "LibLinkData",
                    MODE_PRIVATE
                )

            val registeredEmail =
                preferences.getString(
                    "registeredEmail",
                    null
                )

            if (enteredEmail.isEmpty()) {

                emailEditText.error =
                    "Enter your registered email first"

                emailEditText.requestFocus()

            } else if (enteredEmail != registeredEmail) {

                Toast.makeText(
                    this,
                    "No account was found with this email",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Account verified. Password reset will be handled by the API/email service.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //Register
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser() {

        val email =
            emailEditText.text.toString().trim()

        val password =
            passwordEditText.text.toString()

        if (email.isEmpty()) {

            emailEditText.error =
                "Please enter your student email"

            emailEditText.requestFocus()

            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
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

        val preferences =
            getSharedPreferences(
                "LibLinkData",
                MODE_PRIVATE
            )

        val registeredEmail =
            preferences.getString(
                "registeredEmail",
                null
            )

        val registeredPassword =
            preferences.getString(
                "registeredPassword",
                null
            )

        if (
            email != registeredEmail ||
            password != registeredPassword
        ) {

            Toast.makeText(
                this,
                "Incorrect email or password",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        preferences.edit()
            .putString(
                "loggedInEmail",
                email
            )
            .apply()

        Toast.makeText(
            this,
            "Login Successful",
            Toast.LENGTH_SHORT
        ).show()

        val intent =
            Intent(this, HomeActivity::class.java)

        startActivity(intent)

        finish()
    }
}