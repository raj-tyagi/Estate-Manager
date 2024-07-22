package com.example.databaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseapp.databinding.ActivityStaffLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StaffLoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityStaffLoginBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val staffId = binding.staffIdEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Check if staffId is valid
                    db.collection("staffMembers").document(staffId).get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            // Staff ID is valid, proceed to HomeActivity
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            // Invalid Staff ID
                            Toast.makeText(this, "Invalid Staff ID", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to validate Staff ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerStaffButton.setOnClickListener {
            startActivity(Intent(this, StaffRegisterActivity::class.java))
        }
    }
}
