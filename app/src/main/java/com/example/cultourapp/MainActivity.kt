package com.example.cultourapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.databinding.ActivityMainBinding
import com.example.cultourapp.loginPage.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startAnimations()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3500L)
    }

    private fun startAnimations() {
        binding.textView.visibility = View.INVISIBLE

        val imageAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_Y, 1000f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(binding.imgLogo, View.ALPHA, 0f, 1f).setDuration(900)
            )
        }

        val titleAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.textView, View.TRANSLATION_Y, -1000f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f).setDuration(900)
            )
        }

        AnimatorSet().apply {
            playSequentially(
                imageAnimator,
                AnimatorSet().apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            super.onAnimationStart(animation)
                            binding.textView.visibility = View.VISIBLE
                            binding.imgLogo.visibility = View.VISIBLE
                        }
                    })
                    playTogether(titleAnimator)
                }
            )
            start()
        }
    }
}