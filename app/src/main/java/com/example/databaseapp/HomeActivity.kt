package com.example.databaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buyButton.setOnClickListener {
            startActivity(Intent(this, BuyActivity::class.java))
        }

        binding.rentButton.setOnClickListener {
            startActivity(Intent(this, RentActivity::class.java))
        }

        binding.sellButton.setOnClickListener {
            startActivity(Intent(this, SellActivity::class.java))
        }

        binding.toletButton.setOnClickListener {
            startActivity(Intent(this, ToletActivity::class.java))
        }

        binding.logButton.setOnClickListener {
            startActivity(Intent(this, LogsActivity::class.java))
        }

        binding.toletButton.setOnClickListener {
            val intent = Intent(this, ToletActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.logoutTextView.setOnClickListener {
            // Build the alert dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
            builder.setMessage("Do you want to Log Out?")

            // Set up the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                // Navigate back to previous screen
                super.onBackPressed()
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing (stay on the current screen)
                dialog.dismiss()
            }

            // Create and show the dialog
            val dialog = builder.create()
            dialog.show()
        }
    }


}
