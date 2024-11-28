package com.example.cultourapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.R
import com.example.cultourapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false
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

    private fun validRegister() {
        binding.btnLogin.setOnClickListener {
            if (areFieldsValid()) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val emailText = binding.etEmail.text.toString()
        val passwordText = binding.etPassword.text.toString()

        val isEmailValid = emailText.isNotEmpty()
        val isPasswordValid = passwordText.isNotEmpty()

        return isEmailValid && isPasswordValid
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
                validRegister()
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
                validRegister()
            }
        }
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
                ObjectAnimator.ofFloat(binding.loginImg, View.TRANSLATION_Y, -200f, 0f).setDuration(500),
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