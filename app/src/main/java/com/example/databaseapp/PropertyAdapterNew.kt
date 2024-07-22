package com.example.databaseapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databaseapp.databinding.DialogContactUsBinding
import com.example.databaseapp.databinding.ItemPropertyNewBinding
import com.google.firebase.firestore.FirebaseFirestore

class PropertyAdapterNew(private val context: Context) : RecyclerView.Adapter<PropertyAdapterNew.PropertyViewHolder>() {

    private var properties = listOf<Property>()
    private val db = FirebaseFirestore.getInstance()

    fun submitList(list: List<Property>) {
        properties = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    inner class PropertyViewHolder(private val binding: ItemPropertyNewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.property = property
            binding.executePendingBindings()

            if (property.imageUrl.isNotEmpty()) {
                Glide.with(context).load(property.imageUrl).into(binding.propertyImageView)
            }

            binding.confirmButton.setOnClickListener {
                showContactUsDialog()
            }
        }

        private fun showContactUsDialog() {
            val dialogBinding = DialogContactUsBinding.inflate(LayoutInflater.from(context))
            val dialog = AlertDialog.Builder(context)
                .setTitle("Contact Us")
                .setView(dialogBinding.root)
                .setCancelable(true)
                .create()

            dialogBinding.contactInfoTextView.text = "Phone: xxxxxxx\nEmail: xxx-xxxx-xxx \nAddress: xxxx-xxxx-xxxx"

            dialog.setOnShowListener {
                dialog.window?.decorView?.setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }
}
