package com.example.databaseapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseapp.databinding.ActivitySellBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class SellActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var binding: ActivitySellBinding

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("property_images")

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.submitButton.setOnClickListener {
            uploadProperty()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun uploadProperty() {
        val address = binding.addressEditText.text.toString()
        val contact = binding.ContactEditText.text.toString()
        val owner = binding.OwnerEditText.text.toString()
        val price = binding.priceEditText.text.toString()
        val size = binding.sizeEditText.text.toString()

        if (address.isNotBlank() && contact.isNotBlank() && owner.isNotBlank() && price.isNotBlank() && size.isNotBlank() && selectedImageUri != null) {
            val imageRef = storageRef.child(UUID.randomUUID().toString())
            imageRef.putFile(selectedImageUri!!)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        val property = Property(
                            address = address,
                            contact = contact,
                            owner = owner,
                            price = price,
                            size = size,
                            imageUrl = downloadUri.toString(), // Associate image URL with property
                            type = "sale"
                        )

                        db.collection("properties").add(property)
                            .addOnSuccessListener {
                                Toast.makeText(this@SellActivity, "Property added successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@SellActivity, "Failed to add property", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this@SellActivity, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data
            binding.propertyImageView.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
