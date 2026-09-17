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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.registerName)
        registerEmail = findViewById(R.id.registerEmail)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtBackToLogin = findViewById(R.id.txtBackToLogin)

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

    private fun registerUser(){
        val enteredName = registerName.text.toString().trim()
        val enteredEmail = registerEmail.text.toString().trim()
        val enteredPassword = registerPassword.text.toString()
        val enteredConfirmPassword = registerConfirmPassword.text.toString()

        if (enteredName.isEmpty()){
            registerName.error = "Please enter your name"
            registerName.requestFocus()
            return
        }

        if (enteredName.isEmpty()){
            registerEmail.error = "Please enter your email"
            registerEmail.requestFocus()

            return
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(enteredEmail)
                .matches()
            ){

            registerEmail.error = "Please enter a valid email address"
            registerEmail.requestFocus()

            return
        }

        if (enteredPassword.length < 6){
            registerPassword.error = "Password must be at least 6 characters"

            registerPassword.requestFocus()
            return
        }

        if (enteredPassword != enteredConfirmPassword){
            registerConfirmPassword.error = "Passwords do not match"

            registerConfirmPassword.requestFocus()
            return
        }

        val preferences = getSharedPreferences("LibLinkdata", MODE_PRIVATE)

        preferences.edit().putString("registerName", enteredName)
            .putString("registerEmail", enteredEmail)
            .putString("registerPassword", enteredPassword)
            .apply()

        Toast.makeText(this, "Registration successful. Please sign in", Toast.LENGTH_LONG).show()

        startActivity(Intent(this, MainActivity::class.java))

        finish()

    }

}