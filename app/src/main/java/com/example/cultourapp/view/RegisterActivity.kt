package com.example.cultourapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import com.example.cultourapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private var isPasswordVisible = false

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

        //Action toggle password
        binding.ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ivTogglePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ivTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            }

            binding.etPassword.setSelection(binding.etPassword.text.length)
        }


        //Action toggle re password
        binding.ivToggleRePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                binding.etRePassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ivToggleRePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                binding.etRePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ivToggleRePassword.setImageResource(R.drawable.ic_visibility_off)
            }

            binding.etRePassword.setSelection(binding.etPassword.text.length)
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                startAnimations()
            }
        })
    }

    private fun startAnimations() {
        binding.registerTitle.alpha = 0f
        binding.tvUsername.alpha = 0f
        binding.etUsername.alpha = 0f
        binding.tvEmail.alpha = 0f
        binding.etEmail.alpha = 0f
        binding.tvPassword.alpha = 0f
        binding.etPassword.alpha = 0f
        binding.ivTogglePassword.alpha = 0f
        binding.tvRePassword.alpha = 0f
        binding.etRePassword.alpha = 0f
        binding.ivToggleRePassword.alpha = 0f
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
                        ObjectAnimator.ofFloat(binding.etPassword, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },


                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(
                            binding.ivTogglePassword, View.TRANSLATION_X, -100f, 0f)
                        .setDuration(400),
                        ObjectAnimator.ofFloat(binding.ivTogglePassword, View.ALPHA, 0f, 1f)
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
                        ObjectAnimator.ofFloat(binding.etRePassword, View.TRANSLATION_X, -100f, 0f)
                            .setDuration(400),
                        ObjectAnimator.ofFloat(binding.etRePassword, View.ALPHA, 0f, 1f)
                            .setDuration(400)
                    )
                },

                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(
                            binding.ivToggleRePassword,
                            View.TRANSLATION_X,
                            -100f,
                            0f
                        ).setDuration(400),
                        ObjectAnimator.ofFloat(binding.ivToggleRePassword, View.ALPHA, 0f, 1f)
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
