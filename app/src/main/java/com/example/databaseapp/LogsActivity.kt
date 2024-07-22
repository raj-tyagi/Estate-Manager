package com.example.databaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databaseapp.databinding.ActivityLogsBinding
import com.google.firebase.firestore.FirebaseFirestore

class LogsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var logAdapter: LogAdapter
    private lateinit var binding: ActivityLogsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        logAdapter = LogAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = logAdapter

        db.collection("logs")
            .get()
            .addOnSuccessListener { result ->
                val logs = result.toObjects(Log::class.java)
                logAdapter.submitList(logs)
            }
    }
}
