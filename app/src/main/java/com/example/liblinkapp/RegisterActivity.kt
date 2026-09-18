package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerName : EditText
    private lateinit var registerEmail : EditText
    private lateinit var registerPassword : EditText
    private lateinit var registerConfirmPassword : EditText
    private lateinit var btnRegister : Button
    private lateinit var txtBackToLogin : TextView
    private lateinit var registerStudentNumber : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.registerName)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtBackToLogin = findViewById(R.id.txtBackToLogin)
        registerStudentNumber = findViewById(R.id.registerStudentNumber)

        txtBackToLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }

        btnRegister.setOnClickListener {
            registerUser()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun registerUser() {

        val enteredName =
            registerName.text.toString().trim()

        val enteredEmail =
            registerEmail.text.toString().trim()

        val enteredStudentNumber =
            registerStudentNumber.text.toString().trim()

        val enteredPassword =
            registerPassword.text.toString()

        val enteredConfirmPassword =
            registerConfirmPassword.text.toString()

        if (enteredName.isEmpty()) {
            registerName.error = "Please enter your name"
            registerName.requestFocus()
            return
        }

        if (enteredEmail.isEmpty()) {
            registerEmail.error = "Please enter your email"
            registerEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(enteredEmail)
                .matches()
        ) {
            registerEmail.error = "Please enter a valid email"
            registerEmail.requestFocus()
            return
        }

        if (enteredStudentNumber.isEmpty()) {
            registerStudentNumber.error =
                "Please enter your student number"
            registerStudentNumber.requestFocus()
            return
        }

        if (enteredPassword.length < 6) {
            registerPassword.error =
                "Password must be at least 6 characters"
            registerPassword.requestFocus()
            return
        }

        if (enteredPassword != enteredConfirmPassword) {
            registerConfirmPassword.error =
                "Passwords do not match"
            registerConfirmPassword.requestFocus()
            return
        }

        btnRegister.isEnabled = false

        val api =
            com.example.liblinkapp.api.ApiClient.create(this)

        api.register(
            com.example.liblinkapp.models.RegisterRequest(
                fullName = enteredName,
                email = enteredEmail,
                studentNumber = enteredStudentNumber,
                password = enteredPassword
            )
        ).enqueue(object :
            retrofit2.Callback<com.example.liblinkapp.models.RegisterResponse> {

            override fun onResponse(
                call: retrofit2.Call<com.example.liblinkapp.models.RegisterResponse>,
                response: retrofit2.Response<com.example.liblinkapp.models.RegisterResponse>
            ) {

                btnRegister.isEnabled = true

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successful. Please sign in.",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(
                            this@RegisterActivity,
                            MainActivity::class.java
                        )
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.example.liblinkapp.models.RegisterResponse>,
                t: Throwable
            ) {

                btnRegister.isEnabled = true

                Toast.makeText(
                    this@RegisterActivity,
                    "Could not connect to API: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}