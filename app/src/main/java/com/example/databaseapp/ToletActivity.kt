package com.example.databaseapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseapp.databinding.ActivityToletBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ToletActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityToletBinding
    private var selectedImageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.propertyImageView.setImageURI(selectedImageUri) // Display selected image in ImageView
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("property_images")

        binding.imageButton.setOnClickListener {
            openImageChooser()
        }

        binding.submitButton.setOnClickListener {
            uploadProperty()
        }
    }

    private fun openImageChooser() {
        getContent.launch("image/*")
    }

    private fun uploadProperty() {
        val address = binding.addressEditText.text.toString()
        val contact = binding.contactEditText.text.toString()
        val owner = binding.ownerEditText.text.toString()
        val price = binding.priceEditText.text.toString()
        val size = binding.sizeEditText.text.toString()

        if (address.isNotBlank() && contact.isNotBlank() && owner.isNotBlank() && price.isNotBlank() && size.isNotBlank() && selectedImageUri != null) {
            val imageFileName = System.currentTimeMillis().toString() + ".jpg"
            val property = Property(
                address = address,
                contact = contact,
                owner = owner,
                price = price,
                size = size,
                type = "rent",
                imageUrl = ""
            )

            db.collection("properties").add(property)
                .addOnSuccessListener { documentReference ->
                    val uploadTask = storageReference.child(imageFileName).putFile(selectedImageUri!!)
                    uploadTask.addOnSuccessListener { _ ->
                        storageReference.child(imageFileName).downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            db.collection("properties").document(documentReference.id)
                                .update("imageUrl", imageUrl)
                                .addOnSuccessListener {
                                    Toast.makeText(this@ToletActivity, "Property added successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                    startActivity(Intent(this@ToletActivity, HomeActivity::class.java))
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this@ToletActivity, "Error updating image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this@ToletActivity, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@ToletActivity, "Error adding property: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}
