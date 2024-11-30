package com.example.cultourapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.R
import com.example.cultourapp.databinding.ActivityLoginBinding
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.modelView.AuthViewModel
import com.example.cultourapp.modelView.factory.ViewModelFactory
import com.example.cultourapp.model.pref.UserPreferences

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userRepo: UserRepository
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepo = Injection.provideUserRepository(this)

        val userSession = authViewModel.getUserSession()
        if (userSession.email.isNotEmpty()) {
            goToHome()
        }

        authViewModel.loginResponse.observe(this) { response ->
            response?.let {
                if (it.success) {
                    Log.d("Token", "${it.data?.token}")
                    Toast.makeText(this, "Login successful: ${it.message}", Toast.LENGTH_SHORT).show()
                    goToHome()
                } else {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe loading state
        authViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Navigate to RegisterActivity if sign-up is clicked
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                startAnimations()
            }
        })

        emailFocusedListener()
        passwordFocusedListener()
    }

    private fun validLogin() {
        binding.btnLogin.setOnClickListener {
            if (areFieldsValid()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val userData = mapOf("email" to email, "password" to password)
                authViewModel.loginUser(userData)
            } else {
                Toast.makeText(this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val emailText = binding.etEmail.text.toString()
        val passwordText = binding.etPassword.text.toString()
        return emailText.isNotEmpty() && passwordText.isNotEmpty()
    }

    private fun emailFocusedListener() {
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            val emailText = binding.etEmail.text.toString()
            if (!focused) {
                if (emailText.isEmpty()) {
                    binding.emailInputLayout.error = "Email is required"
                    binding.emailInputLayout.errorIconDrawable = null
                } else {
                    val validationMessage = validEmail()
                    binding.emailInputLayout.error = validationMessage
                    binding.emailInputLayout.errorIconDrawable = null
                    if (validationMessage == null) {
                        binding.emailInputLayout.errorIconDrawable = null
                    }
                }
                validLogin()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.etEmail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else {
            null
        }
    }

    private fun passwordFocusedListener() {
        binding.etPassword.setOnFocusChangeListener { _, focused ->
            val usernameText = binding.etPassword.text.toString()
            if (!focused) {
                if (usernameText.isEmpty()) {
                    binding.passwordInputLayout.error = "Password is required"
                    binding.passwordInputLayout.errorIconDrawable = null
                } else {
                    binding.passwordInputLayout.error = null
                }
                validLogin()
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun startAnimations() {
        binding.loginTitle.alpha = 0f
        binding.loginImg.alpha = 0f
        binding.tvEmail.alpha = 0f
        binding.emailInputLayout.alpha = 0f
        binding.tvPassword.alpha = 0f
        binding.passwordInputLayout.alpha = 0f
        binding.btnLogin.alpha = 0f
        binding.signupSection.alpha = 0f

        val titleAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.loginTitle, View.TRANSLATION_Y, -100f, 0f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 0f, 1f).setDuration(500)
            )
        }

        val imageAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.loginImg, View.TRANSLATION_Y, -100f, 0f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginImg, View.ALPHA, 0f, 1f).setDuration(500)
            )
        }

        val fieldsAnimator = AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvEmail, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.emailInputLayout, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvPassword, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.passwordInputLayout, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.btnLogin, View.TRANSLATION_Y, 50f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                }
            )
        }

        val signupAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.signupSection, View.TRANSLATION_Y, 50f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(binding.signupSection, View.ALPHA, 0f, 1f).setDuration(400)
            )
        }

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                imageAnimator,
                fieldsAnimator,
                signupAnimator
            )
            start()
        }
    }
}
