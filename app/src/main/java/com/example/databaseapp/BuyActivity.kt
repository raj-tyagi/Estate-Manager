// BuyActivity.kt
package com.example.databaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databaseapp.databinding.ActivityBuyBinding
import com.google.firebase.firestore.FirebaseFirestore

class BuyActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var binding: ActivityBuyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        propertyAdapter = PropertyAdapter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = propertyAdapter

        db.collection("properties")
            .whereEqualTo("type", "sale") // Fetching properties for sale
            .get()
            .addOnSuccessListener { result ->
                val properties = result.toObjects(Property::class.java)
                propertyAdapter.submitList(properties)
            }
    }
}
