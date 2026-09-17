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
            Toast.makeText(this, "Password reset function will be available soon.", Toast.LENGTH_LONG).show()
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

    private fun loginUser(){
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        //check email
        if (email.isEmpty()){
            emailEditText.error = "Please enter your student email"
            emailEditText.requestFocus()
            return
        }

        //Check valid email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email address"
            emailEditText.requestFocus()
            return
        }

        //check password
        if (password.isEmpty()){
            passwordEditText.error = "Please enter your password"
            passwordEditText.requestFocus()
            return
        }

        if (password.length < 6 ){
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return
        }

        //Temporary local login.
        //This is where the REST API authentication will be connected for the functional version of libLink

        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()

        val preferences = getSharedPreferences("LibLinkData", MODE_PRIVATE)

        preferences.edit()
            .putString("loggedInEmail", email)
            .apply()

        //Open the main LibLink screen
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        //prevent user from returning to the login screen
        finish()
    }
}