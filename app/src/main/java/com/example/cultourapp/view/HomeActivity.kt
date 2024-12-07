package com.example.cultourapp.view

import java.util.*
import androidx.core.util.Pair
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.R
import com.example.cultourapp.databinding.ActivityHomeBinding
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.UserRepository
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userRepo: UserRepository
    private lateinit var datePicker: MaterialDatePicker<Pair<Long, Long>>
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userRepo = Injection.provideUserRepository(this)
        binding.tvHello.text = "Hello, ${userRepo.getUser().email}"

        binding.btnLogout.setOnClickListener {
            userRepo.clearUserSession()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder.build())

        val weatherSummary = "Partly Cloudy" ///ambil data cuaca dari API

        val weatherText = "${binding.root.context.getString(R.string.weather_summary)} $weatherSummary"
        val weatherError = "Failed to fetch weather data" ///variable untuk menampung bad respond nya dari API


//        binding.tvWeatherText.text = weatherError ---> tampilkan jika user mendapatkan bad response yang dari API nya

        if (weatherSummary.isNotEmpty()) {
            // Jika weatherSummary memiliki isi, tampilkan teks dan ikon
            binding.tvWeatherText.visibility = View.VISIBLE
            binding.tvWeatherText.text = weatherText // Set teks jika belum disetel
            binding.ivWeatherIcon.setImageResource(R.drawable.ic_very_cloudy) // Set gambar ikon sesuai cuaca dari weathersummary nya
            binding.ivWeatherIcon.visibility = View.VISIBLE
        } else {
            // Jika weatherSummary kosong, sembunyikan teks dan ikon
            binding.tvWeatherText.visibility = View.GONE
            binding.ivWeatherIcon.visibility = View.GONE
        }


        builder.setTitleText("Select Date Range")
       // builder.setCalendarConstraints(constraintsBuilder.build()) // Make sure to apply the constraints
        datePicker = builder.build()

        binding.etStartDate.setOnClickListener {
            datePicker.show(supportFragmentManager, "date_picker")
        }

        binding.etEndDate.setOnClickListener {
            datePicker.show(supportFragmentManager, "date_picker")
        }

            // Handle date selection
            datePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selection.first)
                val endDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selection.second)
                binding.etStartDate.setText(startDate)
                binding.etEndDate.setText(endDate)
            }
        }
    }


