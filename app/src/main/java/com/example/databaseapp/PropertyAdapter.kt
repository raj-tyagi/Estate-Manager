package com.example.databaseapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databaseapp.databinding.DialogTransactionFormBinding
import com.example.databaseapp.databinding.ItemPropertyBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PropertyAdapter(private val context: Context) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private var properties = listOf<Property>()
    private val db = FirebaseFirestore.getInstance()

    fun submitList(list: List<Property>) {
        properties = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.property = property
            binding.executePendingBindings()

            if (property.imageUrl.isNotEmpty()) {
                Glide.with(context).load(property.imageUrl).into(binding.propertyImageView)
            }

            binding.confirmButton.setOnClickListener {
                showTransactionForm(property)
            }
        }

        private fun showTransactionForm(property: Property) {
            val formBinding = DialogTransactionFormBinding.inflate(LayoutInflater.from(context))
            val dialog = AlertDialog.Builder(context)
                .setTitle("Property Transaction")
                .setView(formBinding.root)
                .setCancelable(true)
                .create()

            formBinding.submitTransactionButton.setOnClickListener {
                val newOwner = formBinding.newOwnerEditText.text.toString()
                val oldOwner = formBinding.oldOwnerEditText.text.toString()
                val dealAmount = formBinding.dealAmountEditText.text.toString()
                val propertySize = formBinding.propertySizeEditText.text.toString()
                val dealerAgent = formBinding.dealerAgentEditText.text.toString()
                val commissionEarned = (dealAmount.toDoubleOrNull() ?: 0.0) * 0.02

                if (newOwner.isNotBlank() && oldOwner.isNotBlank() && dealAmount.isNotBlank() && propertySize.isNotBlank() && dealerAgent.isNotBlank()) {
                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val log = Log(
                        message = "Property transaction completed",
                        timestamp = timestamp,
                        newOwner = newOwner,
                        oldOwner = oldOwner,
                        dealAmount = dealAmount,
                        propertySize = propertySize,
                        dealerAgent = dealerAgent,
                        commissionEarned = commissionEarned.toString(),
                        propertyType = property.type, // Assuming property has a type field (sale or rent)
                        propertyAddress = property.address,
                        imageUrl = property.imageUrl // Add imageUrl to the log
                    )

                    db.collection("logs").add(log)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Transaction logged", Toast.LENGTH_SHORT).show()
                            db.collection("properties")
                                .whereEqualTo("address", property.address)
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        db.collection("properties").document(document.id).delete()
                                    }
                                    dialog.dismiss()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to log transaction", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
        }
    }
}
