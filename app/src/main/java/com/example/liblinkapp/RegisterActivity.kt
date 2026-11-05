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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.models.RegisterRequest
import com.example.liblinkapp.models.GoogleLoginRequest
import kotlinx.coroutines.launch
import com.example.liblinkapp.sync.SyncScheduler
import com.example.liblinkapp.utils.SessionManager
class RegisterActivity : BaseActivity() {

    private lateinit var registerName: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerConfirmPassword: EditText
    private lateinit var registerStudentNumber: EditText

    private lateinit var btnRegister: Button
    private lateinit var btnGoogleRegister: Button
    private lateinit var txtBackToLogin: TextView

    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.registerName)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerConfirmPassword =
            findViewById(R.id.registerConfirmPassword)

        registerStudentNumber =
            findViewById(R.id.registerStudentNumber)

        btnRegister = findViewById(R.id.btnRegister)
        btnGoogleRegister = findViewById(R.id.btnGoogleRegister)
        txtBackToLogin = findViewById(R.id.txtBackToLogin)

        credentialManager = CredentialManager.create(this)

        btnRegister.setOnClickListener {
            registerUser()
        }

        btnGoogleRegister.setOnClickListener {
            registerWithGoogle()
        }

        txtBackToLogin.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    // --------------------------------
    // NORMAL REGISTRATION
    // --------------------------------

    private fun registerUser() {

        val enteredName = registerName.text.toString().trim()
        val enteredEmail = registerEmail.text.toString().trim()
        val enteredStudentNumber =
            registerStudentNumber.text.toString().trim()

        val enteredPassword = registerPassword.text.toString()
        val enteredConfirmPassword =
            registerConfirmPassword.text.toString()

        if (enteredName.isEmpty()) {
            registerName.error = "Please enter your name"
            registerName.requestFocus()
            return
        }

        if (enteredEmail.isEmpty() ||
            !Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()
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

        val api = ApiClient.create(this)

        val request = RegisterRequest(
            fullName = enteredName,
            email = enteredEmail,
            studentNumber = enteredStudentNumber,
            password = enteredPassword
        )

        lifecycleScope.launch {
            try {
                val response = api.register(request)

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
                    val errorMessage =
                        response.errorBody()?.string()
                            ?: "Registration failed"

                    Toast.makeText(
                        this@RegisterActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {
                btnRegister.isEnabled = true
            }
        }
    }

    // --------------------------------
    // GOOGLE REGISTRATION
    // --------------------------------

    private fun registerWithGoogle() {

        val studentNumber =
            registerStudentNumber.text.toString().trim()

        if (studentNumber.isEmpty()) {
            registerStudentNumber.error =
                "Please enter your student number first"
            registerStudentNumber.requestFocus()
            return
        }

        btnGoogleRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val googleIdOption =
                    GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(
                            getString(R.string.default_web_client_id)
                        )
                        .setAutoSelectEnabled(false)
                        .build()

                val request =
                    GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                val result = credentialManager.getCredential(
                    context = this@RegisterActivity,
                    request = request
                )

                val credential = result.credential

                if (
                    credential is CustomCredential &&
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        GoogleIdTokenCredential.createFrom(
                            credential.data
                        )

                    authenticateGoogleWithBackend(
                        googleCredential.idToken,
                        studentNumber
                    )

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Invalid Google credential.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: NoCredentialException) {

                Toast.makeText(
                    this@RegisterActivity,
                    "No Google account credentials available.",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: GetCredentialException) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Google sign-in unavailable: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Google registration failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {
                btnGoogleRegister.isEnabled = true
            }
        }
    }

    // --------------------------------
    // SEND GOOGLE TOKEN TO AZURE API
    // --------------------------------

    private suspend fun authenticateGoogleWithBackend(
        idToken: String,
        studentNumber: String
    ) {

        try {
            val api = ApiClient.create(this)

            val request = GoogleLoginRequest(
                idToken = idToken,
                studentNumber = studentNumber
            )

            val response = api.googleLogin(request)

            if (!response.isSuccessful) {

                val errorMessage =
                    response.errorBody()?.string()
                        ?: "Google authentication failed."

                Toast.makeText(
                    this,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            val result = response.body()

            if (result == null) {
                Toast.makeText(
                    this,
                    "Empty response from API.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if (result.requiresRegistration) {
                Toast.makeText(
                    this,
                    result.message
                        ?: "Please complete your registration.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            val token = result.token

            if (token.isNullOrBlank() || result.userId!! <= 0) {
                Toast.makeText(
                    this,
                    "Invalid login response from API.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            // Save LibLink authentication session.
            val sessionManager = SessionManager(this)

            sessionManager.saveSession(
                result.userId,
                result.name.orEmpty(),
                result.email.orEmpty(),
                result.role ?: "Student",
                token
            )

            // Schedule background synchronisation.
            SyncScheduler.scheduleSync(this)

            Toast.makeText(
                this,
                "Google registration/login successful!",
                Toast.LENGTH_LONG
            ).show()

            // Open LibLink home screen.
            startActivity(
                Intent(this, HomeActivity::class.java)
            )

            finish()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "API error: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}