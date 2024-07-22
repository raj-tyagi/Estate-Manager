package com.example.databaseapp

data class Property(
    val address: String = "",
    val contact: String = "",
    val owner: String = "",
    val price: String = "",
    val size: String = "",
    val type: String = "", // Assuming type is sale/rent
    val imageUrl: String = ""
)

