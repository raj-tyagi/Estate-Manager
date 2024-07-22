package com.example.databaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databaseapp.databinding.ActivityUserHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var propertyAdapterSale: PropertyAdapterNew
    private lateinit var propertyAdapterRent: PropertyAdapterNew
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        propertyAdapterSale = PropertyAdapterNew(this)
        propertyAdapterRent = PropertyAdapterNew(this)

        binding.recyclerViewSale.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewSale.adapter = propertyAdapterSale

        binding.recyclerViewRent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRent.adapter = propertyAdapterRent

        loadProperties()
    }

    private fun loadProperties() {
        db.collection("properties")
            .whereEqualTo("type", "sale")
            .get()
            .addOnSuccessListener { documents ->
                val saleProperties = documents.toObjects(Property::class.java)
                propertyAdapterSale.submitList(saleProperties)
            }

        db.collection("properties")
            .whereEqualTo("type", "rent")
            .get()
            .addOnSuccessListener { documents ->

                val rentProperties = documents.toObjects(Property::class.java)
                propertyAdapterRent.submitList(rentProperties)
            }
    }
}
