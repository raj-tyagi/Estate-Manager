package com.example.databaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databaseapp.databinding.ActivityRentBinding
import com.google.firebase.firestore.FirebaseFirestore

class RentActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var binding: ActivityRentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        propertyAdapter = PropertyAdapter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = propertyAdapter

        db.collection("properties")
            .whereEqualTo("type", "rent") // Fetching properties for rent
            .get()
            .addOnSuccessListener { result ->
                val properties = result.toObjects(Property::class.java)
                propertyAdapter.submitList(properties)
            }
    }
}
