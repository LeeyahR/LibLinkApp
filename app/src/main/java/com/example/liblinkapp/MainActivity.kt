
package com.example.liblinkapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope

import com.example.liblinkapp.api.ApiClient
import com.example.liblinkapp.api.ApiService
import com.example.liblinkapp.models.LoginRequest
import com.example.liblinkapp.models.GoogleLoginRequest
import com.example.liblinkapp.models.GoogleLoginResponse
import com.example.liblinkapp.utils.SessionManager

import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

import kotlinx.coroutines.launch
import com.example.liblinkapp.sync.SyncScheduler

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnSignIn: Button
    private lateinit var btnSSO: Button
    private lateinit var forgotPassword: TextView
    private lateinit var registerText: TextView

    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        api = ApiClient.create(this)
        sessionManager = SessionManager(this)
        credentialManager = CredentialManager.create(this)


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnSSO = findViewById(R.id.btnSSO)
        forgotPassword = findViewById(R.id.forgotPassword)
        registerText = findViewById(R.id.registerText)

        // NORMAL LOGIN
        btnSignIn.setOnClickListener {
            loginUser()
        }

        // GOOGLE SSO
        btnSSO.setOnClickListener {
            signInWithGoogle()
        }

        // FORGOT PASSWORD
        forgotPassword.setOnClickListener {
            Toast.makeText(
                this,
                "Password reset will be handled by the API/email service.",
                Toast.LENGTH_LONG
            ).show()
        }

        // REGISTER
        registerText.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        // BIOMETRIC SESSION UNLOCK
        if (sessionManager.isLoggedIn()) {
            if (sessionManager.isBiometricEnabled()) {
                showBiometricPrompt {
                    navigateToHome()
                }
            } else {
                navigateToHome()
            }
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

    // =====================================
    // BIOMETRIC AVAILABILITY
    // =====================================

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)

        return when (
            biometricManager.canAuthenticate(
                Authenticators.BIOMETRIC_STRONG or
                        Authenticators.BIOMETRIC_WEAK
            )
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> true

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    this,
                    "This device does not support biometric authentication.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    this,
                    "Biometric hardware is currently unavailable.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    this,
                    "No fingerprint or face is enrolled on this device.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            else -> {
                Toast.makeText(
                    this,
                    "Biometric authentication is not available.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
    }

    // =====================================
    // BIOMETRIC AUTHENTICATION PROMPT
    // =====================================

    private fun showBiometricPrompt(
        onAuthenticationSuccess: () -> Unit
    ) {
        if (!isBiometricAvailable()) {
            return
        }

        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    Toast.makeText(
                        this@MainActivity,
                        "Biometric authentication successful.",
                        Toast.LENGTH_SHORT
                    ).show()

                    onAuthenticationSuccess()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                    Toast.makeText(
                        this@MainActivity,
                        errString,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                    Toast.makeText(
                        this@MainActivity,
                        "Fingerprint not recognised. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("LibLink biometric login")
            .setSubtitle("Verify your identity to continue")
            .setNegativeButtonText("Use password")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun offerBiometricSetup(onContinue: () -> Unit) {
        if (!isBiometricAvailable()) {
            // Biometrics aren't available or enrolled; continue with normal login.
            onContinue()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Enable biometric login?")
            .setMessage(
                "Would you like to use your fingerprint or face to unlock LibLink next time?"
            )
            .setPositiveButton("Enable") { _, _ ->
                sessionManager.setBiometricEnabled(true)
                Toast.makeText(
                    this,
                    "Biometric login enabled.",
                    Toast.LENGTH_SHORT
                ).show()
                onContinue()
            }
            .setNegativeButton("Not now") { _, _ ->
                sessionManager.setBiometricEnabled(false)
                onContinue()
            }
            .setCancelable(false)
            .show()
    }

    // =====================================
    // GOOGLE SIGN IN
    // =====================================

    private fun signInWithGoogle() {

        btnSSO.isEnabled = false

        lifecycleScope.launch {

            try {

                val googleOption =
                    GetSignInWithGoogleOption.Builder(
                        getString(R.string.default_web_client_id)
                    )
                        .build()

                val request =
                    GetCredentialRequest.Builder()
                        .addCredentialOption(googleOption)
                        .build()

                val result = credentialManager.getCredential(
                    context = this@MainActivity,
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

                    Log.d(
                        "LibLinkGoogleSSO",
                        "Google ID token retrieved successfully"
                    )

                    authenticateWithBackend(
                        googleCredential.idToken
                    )

                } else {

                    Log.e(
                        "LibLinkGoogleSSO",
                        "Unexpected credential type: ${credential.type}"
                    )

                    Toast.makeText(
                        this@MainActivity,
                        "Unexpected Google credential.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: GetCredentialException) {

                Log.e(
                    "LibLinkGoogleSSO",
                    "Credential error: ${e.javaClass.simpleName}: ${e.message}",
                    e
                )

                Toast.makeText(
                    this@MainActivity,
                    "Google sign-in failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {

                Log.e(
                    "LibLinkGoogleSSO",
                    "Unexpected error",
                    e
                )

                Toast.makeText(
                    this@MainActivity,
                    "Google sign-in failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSSO.isEnabled = true
            }
        }
    }

    // =====================================
    // AUTHENTICATE WITH LIBLINK API
    // =====================================

    private suspend fun authenticateWithBackend(
        idToken: String,
        studentNumber: String? = null
    ) {

        try {

            val request = GoogleLoginRequest(
                idToken = idToken,
                studentNumber = studentNumber
            )

            val response = api.googleLogin(request)

            if (!response.isSuccessful) {

                val error = response.errorBody()
                    ?.string()
                    ?: "Google authentication failed."

                Toast.makeText(
                    this,
                    error,
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            val result = response.body()

            if (result == null) {

                Toast.makeText(
                    this,
                    "Empty response from LibLink API.",
                    Toast.LENGTH_LONG
                ).show()

                return
            }

            // NEW GOOGLE USER
            if (result.requiresRegistration) {

                showStudentNumberDialog(idToken)
                return
            }

            // EXISTING OR NEWLY REGISTERED USER
            completeLogin(result)
            SyncScheduler.scheduleSync(this)

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Could not connect to LibLink API: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // =====================================
    // NEW USER REGISTRATION
    // =====================================

    private fun showStudentNumberDialog(
        idToken: String
    ) {

        val studentNumberInput = EditText(this).apply {
            hint = "Enter your student number"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        AlertDialog.Builder(this)
            .setTitle("Complete LibLink Registration")
            .setMessage(
                "Your Google account is verified. " +
                        "Please enter your university student number."
            )
            .setView(studentNumberInput)
            .setCancelable(false)
            .setPositiveButton("Continue", null)
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(
                    this,
                    "Registration cancelled.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .create()
            .also { dialog ->

                dialog.setOnShowListener {

                    dialog.getButton(
                        AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener {

                        val studentNumber =
                            studentNumberInput.text
                                .toString()
                                .trim()

                        if (studentNumber.isEmpty()) {

                            studentNumberInput.error =
                                "Student number is required."

                            return@setOnClickListener
                        }

                        dialog.dismiss()

                        lifecycleScope.launch {
                            authenticateWithBackend(
                                idToken,
                                studentNumber
                            )
                        }
                    }
                }

                dialog.show()
            }
    }

    // =====================================
    // COMPLETE LOGIN
    // =====================================

    private fun completeLogin(
        response: GoogleLoginResponse
    ) {

        val token = response.token

        if (token.isNullOrBlank()) {

            Toast.makeText(
                this,
                "LibLink did not return an authentication token.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val userId = response.userId

        if (userId == null || userId <= 0) {

            Toast.makeText(
                this,
                "Invalid LibLink user account.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        sessionManager.saveSession(
            userId = userId,
            name = response.name.orEmpty(),
            email = response.email.orEmpty(),
            role = response.role ?: "Student",
            token = token
        )
        SyncScheduler.scheduleSync(this)

        offerBiometricSetup {
            Toast.makeText(
                this@MainActivity,
                "Google login successful",
                Toast.LENGTH_SHORT
            ).show()

            navigateToHome()
        }
    }

    // =====================================
    // NORMAL EMAIL/PASSWORD LOGIN
    // =====================================

    private fun loginUser() {

        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        if (email.isEmpty()) {
            emailEditText.error = "Please enter your student email"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email address"
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Please enter your password"
            return
        }

        btnSignIn.isEnabled = false

        lifecycleScope.launch {

            try {

                val response = api.login(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )

                if (response.isSuccessful) {

                    val result = response.body()

                    if (result != null) {

                        sessionManager.saveSession(
                            userId = result.userId,
                            name = result.name,
                            email = result.email,
                            role = result.role,
                            token = result.token
                        )

                        offerBiometricSetup {
                            Toast.makeText(
                                this@MainActivity,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToHome()
                        }

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Invalid API response.",
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

                Toast.makeText(
                    this@MainActivity,
                    "Could not connect to API: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSignIn.isEnabled = true
            }
        }
    }

    // =====================================
    // NAVIGATION
    // =====================================

    private fun navigateToHome() {

        startActivity(
            Intent(
                this,
                HomeActivity::class.java
            )
        )

        finish()
    }
}
