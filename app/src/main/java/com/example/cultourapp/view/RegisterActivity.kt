package com.example.cultourapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
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
import com.example.cultourapp.databinding.ActivityRegisterBinding
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.modelView.AuthViewModel
import com.example.cultourapp.modelView.factory.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepo = Injection.provideUserRepository(this)
        authViewModel.registerResponse.observe(this) { response ->
            response?.let {
                if (it.success) {
                    Toast.makeText(this, "Registration successful: ${it.message}", Toast.LENGTH_SHORT).show()
                    goToLoginPage()
                } else {
                    Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        authViewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        binding.btnSignUp.setOnClickListener {
            if (areFieldsValid()) {
                val userData = mapOf(
                    "username" to binding.etUsername.text.toString(),
                    "email" to binding.etEmail.text.toString(),
                    "password" to binding.etPassword.text.toString()
                )
                authViewModel.registerUser(userData)
            } else {
                Toast.makeText(this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                startAnimations()
            }
        })


        emailFocusedListener()
        usernameFocusedListener()
        passwordFocusedListener()
        rePasswordFocusedListener()

    }


    private fun areFieldsValid(): Boolean {
        val usernameText = binding.etUsername.text.toString()
        val emailText = binding.etEmail.text.toString()
        val passwordText = binding.etPassword.text.toString()
        val rePasswordText = binding.etRePassword.text.toString()

        val isUsernameValid = usernameText.isNotEmpty()
        val isEmailValid = emailText.isNotEmpty() && validEmail() == null
        val isPasswordValid = passwordText.isNotEmpty() && validPassword() == null
        val isRePasswordValid = rePasswordText.isNotEmpty() && confirmPassword() == null

        return isUsernameValid && isEmailValid && isPasswordValid && isRePasswordValid
    }

    private fun goToLoginPage() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun usernameFocusedListener() {
        binding.etUsername.setOnFocusChangeListener { _, focused ->
            val usernameText = binding.etUsername.text.toString()
            if (!focused) {
                if (usernameText.isEmpty()) {
                    binding.usernameInputLayout.error = "Username is required"
                    binding.usernameInputLayout.errorIconDrawable = null
                } else {
                    binding.usernameInputLayout.error = null
                }
            }
        }
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
                    if (validationMessage == null) {
                        binding.emailInputLayout.errorIconDrawable = null
                    }
                }
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
            val passwordText = binding.etPassword.text.toString()
            if (!focused) {
                if (passwordText.isEmpty()) {
                    binding.passwordInputLayout.error = "Password is required"
                    binding.passwordInputLayout.errorIconDrawable = null
                } else {
                    val validationMessage = validPassword()
                    binding.passwordInputLayout.error = validationMessage
                    if (validationMessage == null) {
                        binding.passwordInputLayout.errorIconDrawable = null
                    }
                }
            }
        }
    }

    private fun rePasswordFocusedListener() {
        binding.etRePassword.setOnFocusChangeListener { _, focused ->
            val rePasswordText = binding.etRePassword.text.toString()
            if (!focused) {
                if (rePasswordText.isEmpty()) {
                    binding.rePasswordInputLayout.error = "Confirm Your Password"
                    binding.rePasswordInputLayout.errorIconDrawable = null
                } else {
                    val validationMessage = confirmPassword()
                    binding.rePasswordInputLayout.error = validationMessage
                    if (validationMessage == null) {
                        binding.rePasswordInputLayout.errorIconDrawable = null
                    }
                }
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.etPassword.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        return null
    }

    private fun confirmPassword(): String? {
        val passwordText = binding.etPassword.text.toString()
        val rePasswordText = binding.etRePassword.text.toString()
        if (passwordText != rePasswordText) {
            return "Password not match"
        }
        return null
    }


    private fun startAnimations() {
        binding.registerTitle.alpha = 0f
        binding.tvUsername.alpha = 0f
        binding.etUsername.alpha = 0f
        binding.tvEmail.alpha = 0f
        binding.etEmail.alpha = 0f
        binding.tvPassword.alpha = 0f
        binding.passwordInputLayout.alpha = 0f
        binding.tvRePassword.alpha = 0f
        binding.rePasswordInputLayout.alpha = 0f
        binding.btnSignUp.alpha = 0f
        binding.loginSection.alpha = 0f

        val titleAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.registerTitle, View.TRANSLATION_Y, -100f, 0f)
                    .setDuration(500),
                ObjectAnimator.ofFloat(binding.registerTitle, View.ALPHA, 0f, 1f).setDuration(500)
            )
        }


        val fieldsAnimator = AnimatorSet().apply {
            playSequentially(
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvUsername, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.etUsername, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.etUsername, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvEmail, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.etEmail, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvPassword, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.passwordInputLayout, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.tvRePassword, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvRePassword, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.rePasswordInputLayout, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.rePasswordInputLayout, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.btnSignUp, View.TRANSLATION_Y, 50f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                }
            )
        }

        val loginAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.loginSection, View.TRANSLATION_Y, 50f, 0f)
                    .setDuration(400),
                ObjectAnimator.ofFloat(binding.loginSection, View.ALPHA, 0f, 1f).setDuration(400)
            )
        }

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                fieldsAnimator,
                loginAnimator
            )
            start()
        }
    }
}
