package com.example.cultourapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.databinding.ActivityMainBinding
import com.example.cultourapp.view.LoginActivity

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
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                startAnimations()
            }
        })

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3500L)
    }

    private fun startAnimations() {
        binding.textView.alpha = 0f
        binding.imgLogo.alpha = 0f

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
                titleAnimator
            )
            start()
        }
    }
}