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

        binding.ivLogout.setOnClickListener {
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

        // Handle UI weather
        val weatherSummary = "Partly Cloudy" ///ambil data cuaca dari API
        val weatherText = "${binding.root.context.getString(R.string.weather_summary)} $weatherSummary"
        val weatherError = "Failed to fetch weather data" //variable untuk menampung bad respond nya dari API

        // Jika weatherSummary memiliki isi, tampilkan teks dan ikon
        if (weatherSummary.isNotEmpty()) {
            binding.tvWeatherText.visibility = View.VISIBLE
            binding.tvWeatherText.text = weatherText // Set teks jika belum disetel
            binding.ivWeatherIcon.setImageResource(R.drawable.ic_very_cloudy) // Set gambar ikon sesuai cuaca dari weathersummary nya
            binding.ivWeatherIcon.visibility = View.VISIBLE
        } else {
            binding.tvWeatherText.visibility = View.GONE
            binding.ivWeatherIcon.visibility = View.GONE
        }
        //


        //Handle UI chatbot
        val plotUrl = "https://plotly.com/~cufflinks/8" //ambil data plot url dari API
        binding.tvChatBotUrl.text = binding.tvChatBotUrl.text.toString() + plotUrl  //tambahkan teks dengan plot url

        val botResponse = "Expect a drizzley day ahead. Make sure to pack a sturdy umbrella and wear water-resistant clothing. Drive carefully on wet roads, and remember to keep your electronics in waterproof bags."
        ///ambil data chatbot response dari API

        val welcomeText = "I'm your personal travel assistant. I can help you with weather updates, travel norms, and more." ///welcome text untuk chatbot
        val botNorm = "Visitors to Bali should cover their shoulders and knees when entering temples, remain silent during religious ceremonies, and observe local customs with respect and humility."
        ///ambil data chatbot norm dari API


        if (botResponse.isNotEmpty()) { ///buat jika chatbot response nya tidak kosong
            binding.ivChatBotUrl.visibility = View.VISIBLE
            binding.tvChatBotUrl.visibility = View.VISIBLE
            binding.tvChatBotUrl.text = binding.tvChatBotUrl.text.toString() + plotUrl  //tambahkan teks dengan plot url

            binding.ivChatBotResponse.visibility = View.VISIBLE
            binding.tvChatBotResponse.visibility = View.VISIBLE
            binding.tvChatBotResponse.text = binding.tvChatBotResponse.text.toString() + botResponse //tambahkan teks dengan bot response


            binding.ivChatBotNorm.visibility = View.VISIBLE
            binding.tvChatBotNorm.visibility = View.VISIBLE
            binding.tvChatBotNorm.text = binding.tvChatBotNorm.text.toString() + botNorm //tambahkan teks dengan bot norm
        }
        //

        builder.setTitleText("Select Date Range")
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
    } // Closing bracket for onCreate method
} // Closing bracket for HomeActivity class
