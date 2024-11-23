package com.example.cultourapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
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
        val etPassword: EditText = findViewById(R.id.etPassword)
        val ivTogglePassword: ImageView = findViewById(R.id.ivTogglePassword)

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            }

            etPassword.setSelection(etPassword.text.length)
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                startAnimations()
            }
        })
    }

    private fun startAnimations() {
        binding.loginTitle.alpha = 0f
        binding.loginImg.alpha = 0f
        binding.tvUsername.alpha = 0f
        binding.etUsername.alpha = 0f
        binding.tvPassword.alpha = 0f
        binding.etPassword.alpha = 0f
        binding.ivTogglePassword.alpha = 0f
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
                        ObjectAnimator.ofFloat(binding.tvUsername, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.etUsername, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.etUsername, View.ALPHA, 0f, 1f).setDuration(400)
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
                        ObjectAnimator.ofFloat(binding.etPassword, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 0f, 1f).setDuration(400)
                    )
                },
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.ivTogglePassword, View.TRANSLATION_X, -100f, 0f).setDuration(400),
                        ObjectAnimator.ofFloat(binding.ivTogglePassword, View.ALPHA, 0f, 1f).setDuration(400)
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