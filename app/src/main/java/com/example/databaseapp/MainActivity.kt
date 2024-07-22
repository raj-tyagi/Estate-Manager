package com.example.databaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userButton.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
        }

        binding.staffButton.setOnClickListener {
            startActivity(Intent(this, StaffLoginActivity::class.java))
        }
    }
}
