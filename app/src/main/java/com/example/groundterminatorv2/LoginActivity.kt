package com.example.groundterminatorv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogIn = findViewById<Button>(R.id.btnlog)
        val etUsername = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val etPassword = findViewById<EditText>(R.id.editTextTextPassword)

        btnLogIn.setOnClickListener {
            Toast.makeText(this, "Button pressed.", Toast.LENGTH_SHORT).show()

            if (etUsername.text.toString() == "daddy" && etPassword.text.toString() == "123") {

                Intent(this, CameraActivity::class.java).also {
                    startActivity(it)
                }

            } else {
                Toast.makeText(this, "Credentials invalid.", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
