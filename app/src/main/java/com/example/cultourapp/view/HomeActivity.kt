package com.example.cultourapp.view

import java.util.*
import androidx.core.util.Pair
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultourapp.R
import com.example.cultourapp.databinding.ActivityHomeBinding
import com.example.cultourapp.model.di.Injection
import com.example.cultourapp.model.repository.UserRepository
import com.example.cultourapp.model.response.Response
import com.example.cultourapp.modelView.WeatherViewModel
import com.example.cultourapp.modelView.factory.WeatherFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.http.Body
import java.text.SimpleDateFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userRepo: UserRepository
    private lateinit var datePicker: MaterialDatePicker<Pair<Long, Long>>

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherFactory.getInstance(this)
    }

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
        binding.ivLogout.setOnClickListener {
            userRepo.clearUserSession()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Search bar setup
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchWeatherData(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder.build())

        builder.setTitleText("Select Date Range")
        datePicker = builder.build()

        binding.etStartDate.setOnClickListener {
            datePicker.show(supportFragmentManager, "date_picker")
        }

        binding.etEndDate.setOnClickListener {
            datePicker.show(supportFragmentManager, "date_picker")
        }

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDateDefaultFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selection.first)
            val endDateDefaultFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selection.second)

            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val startDate = outputFormat.format(inputFormat.parse(startDateDefaultFormat)!!)
            val endDate = outputFormat.format(inputFormat.parse(endDateDefaultFormat)!!)

            binding.etStartDate.setText(startDate)
            binding.etEndDate.setText(endDate)
        }

        // Observe the LiveData for the API response
        weatherViewModel.chatbotResponse.observe(this) { response ->
            // Handle the API response
            if (response != null) {

                // Update UI elements with weather information
                binding.tvWeatherText.visibility = View.VISIBLE
                binding.ivWeatherIcon.visibility = View.VISIBLE
                binding.tvWeatherText.text = "In the whole range of the date, the weather will be: ${response.weatherSummary}"
                binding.tvChatBotUrl.visibility = View.VISIBLE
                binding.tvChatBotUrl.text = "Here are the summary of the weather report in your given range:${response.plotUrl} "
                binding.tvChatBotResponse.visibility = View.VISIBLE
                binding.tvChatBotResponse.text = response.chatbotResponse
                binding.tvChatBotNorm.visibility = View.VISIBLE
                binding.tvChatBotNorm.text =response.culturalNorm
                val weatherIcon = when (response.weatherSummary) {
                    "Partly cloudy" -> R.drawable.ic_cloudy
                    "Partly rainy" -> R.drawable.ic_rainshower
                    "Partly sunny" -> R.drawable.ic_sunnycloudy
                    "Mostly rainy" -> R.drawable.ic_rainy
                    "Mostly cloudy" -> R.drawable.ic_rainy
                    "Mostly sunny" -> R.drawable.ic_sunny
                    else -> R.drawable.ic_sunny

                }
                binding.ivWeatherIcon.setImageResource(weatherIcon)
            }
            else {
                binding.ivWeatherIcon.visibility = View.VISIBLE
                Toast.makeText(this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeatherData(province: String) {
        val startDate = binding.etStartDate.text.toString()
        val endDate = binding.etEndDate.text.toString()

        if (startDate.isBlank() || endDate.isBlank()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val responseBody = Response(
            province = province,
            startDate = startDate,
            endDate = endDate
        )

        weatherViewModel.getChatbot(responseBody)
    }
}