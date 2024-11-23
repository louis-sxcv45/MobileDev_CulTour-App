package com.example.cultourapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
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
    private var isRePasswordVisible = false

    @SuppressLint("ClickableViewAccessibility")
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

        binding.ivTogglePassword.setOnClickListener {
            togglePasswordVisibility(binding.etPassword, binding.ivTogglePassword)
        }

        binding.ivToggleRePassword.setOnClickListener {
            togglePasswordVisibility(binding.etRePassword, binding.ivToggleRePassword)
        }
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {
        if (editText == binding.etPassword) {
            isPasswordVisible = !isPasswordVisible
            editText.inputType = if (isPasswordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            imageView.setImageResource(
                if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
            )
        } else {
            isRePasswordVisible = !isRePasswordVisible
            editText.inputType = if (isRePasswordVisible) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            imageView.setImageResource(
                if (isRePasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
            )
        }

        editText.setSelection(editText.text.length)
    }
}
