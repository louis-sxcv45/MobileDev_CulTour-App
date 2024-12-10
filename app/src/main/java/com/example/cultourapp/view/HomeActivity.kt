package com.example.cultourapp.view

import android.annotation.SuppressLint
import android.content.Context
import java.util.*
import androidx.core.util.Pair
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.example.cultourapp.model.repository.WeatherRepository
import com.example.cultourapp.model.response.ChatbotResponse
import com.example.cultourapp.modelView.WeatherViewModel
import com.example.cultourapp.modelView.factory.WeatherFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userRepo: UserRepository
    private lateinit var weatherRepo: WeatherRepository
    private lateinit var datePicker: MaterialDatePicker<Pair<Long, Long>>

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherFactory.getInstance(this)
    }

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
        weatherRepo = Injection.provideWeatherRepository(this)

        binding.ivLogout.setOnClickListener {
            userRepo.clearUserSession()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchWeatherData(it) }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.search.windowToken, 0)

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

        binding.etStartDate.setOnClickListener { datePicker.show(supportFragmentManager, "date_picker") }
        binding.etEndDate.setOnClickListener { datePicker.show(supportFragmentManager, "date_picker") }

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate = dateFormat.format(Date(selection.first))
            val endDate = dateFormat.format(Date(selection.second))

            binding.etStartDate.setText(startDate)
            binding.etEndDate.setText(endDate)
        }

        observeWeatherResponse()
    }

    private fun observeWeatherResponse() {
        weatherViewModel.chatbotResponse.observe(this) { response ->
            resetWeatherUI()
            if (response != null) {
                updateWeatherUI(response)
            } else {
                val errorMessage = weatherViewModel.errorMessage.value ?: "Invalid location or date range"
                binding.tvWeatherText.text = "Error: $errorMessage"
                binding.tvWeatherText.visibility = View.GONE
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("HomeActivity", "Error response: $errorMessage")
            }
        }

        weatherViewModel.errorMessage.observe(this) { error ->
            binding.progressBar.visibility = View.GONE
            if (!error.isNullOrEmpty()) {
                binding.tvWeatherText.text = "Error: $error"
                binding.tvWeatherText.visibility = View.VISIBLE
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                Log.e("HomeActivity", "Error: $error")
            }
        }

        weatherViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resetWeatherUI() {
        binding.apply {
            tvWeatherText.text = ""
            ivWeatherIcon.setImageResource(0)
            tvChatBotUrl.text = ""
            ivChatBotUrl.setVisibility(View.INVISIBLE)
            tvChatBotResponse.text = ""
            ivChatBotResponse.setVisibility(View.INVISIBLE)
            tvChatBotNorm.text = ""
            ivChatBotNorm.setVisibility(View.INVISIBLE)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateWeatherUI(response: ChatbotResponse) {
        binding.apply {
            val weatherSummary = response.weatherSummary ?: "Unknown weather"
            val plotUrl = response.plotUrl ?: "No URL available"
            val chatbotResponse = response.chatbotResponse ?: "No chatbot response available"
            val culturalNorm = response.culturalNorm ?: "No cultural norm available"

            tvWeatherText.visibility = View.VISIBLE
            ivWeatherIcon.visibility = View.VISIBLE
            tvWeatherText.text = getString(R.string.weather_summary) + ": " + weatherSummary
            Log.d("HomeActivity", "Response: $weatherSummary")

            tvChatBotUrl.visibility = View.VISIBLE
            ivChatBotUrl.visibility = View.VISIBLE
            tvChatBotUrl.text = getString(R.string.chatbot_url_text) + ": " + plotUrl

            tvChatBotResponse.visibility = View.VISIBLE
            ivChatBotResponse.visibility = View.VISIBLE
            tvChatBotResponse.text = getString(R.string.chatbot_response_text) + ": " + chatbotResponse

            tvChatBotNorm.visibility = View.VISIBLE
            ivChatBotNorm.visibility = View.VISIBLE
            tvChatBotNorm.text = getString(R.string.chatbot_norm_text) + ": " + culturalNorm

            val weatherIcon = when (weatherSummary) {
                "Partly cloudy" -> R.drawable.ic_cloudy
                "Partly rainy" -> R.drawable.ic_rainshower
                "Partly sunny" -> R.drawable.ic_sunnycloudy
                "Mostly rainy" -> R.drawable.ic_rainy
                "Mostly cloudy" -> R.drawable.ic_rainy
                "Mostly sunny" -> R.drawable.ic_sunny
                else -> R.drawable.ic_sunny
            }
            ivWeatherIcon.setImageResource(weatherIcon)
        }
    }

    private fun fetchWeatherData(province: String) {
        val startDate = binding.etStartDate.text.toString()
        val endDate = binding.etEndDate.text.toString()

        if (startDate.isBlank() || endDate.isBlank()) {
            Toast.makeText(this, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        resetWeatherUI()

        weatherViewModel.getChatbot(province, startDate, endDate)
        Log.d("HomeActivity", "Fetching data for $province, from $startDate to $endDate")
    }
}
