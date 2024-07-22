package com.example.databaseapp
data class Log(
    val message: String = "",
    val timestamp: String = "",
    val newOwner: String = "",
    val oldOwner: String = "",
    val dealAmount: String = "",
    val propertySize: String = "",
    val dealerAgent: String = "",
    val commissionEarned: String = "",
    val propertyType: String = "",
    val propertyAddress: String = "",
    val imageUrl: String = "" // Ensure this field exists
)

