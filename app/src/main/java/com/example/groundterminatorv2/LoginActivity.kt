package com.example.groundterminatorv2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.groundterminatorv2.CameraActivity
import com.example.groundterminatorv2.R
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnlog = findViewById<Button>(R.id.btnlog)
        val usernameField = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passField = findViewById<EditText>(R.id.editTextTextPassword)
        btnlog.setOnClickListener {
            Toast.makeText(this, "Button pressed.", Toast.LENGTH_SHORT).show()
            println("ABCD ${passField.text.toString()}  ${usernameField.text.toString()}")
            if (usernameField.text.toString() == "daddy" && passField.text.toString() == "123") {
                Toast.makeText(this, "Stuff happened", Toast.LENGTH_SHORT).show()
                val logIntentCam = Intent(this, CameraActivity::class.java)
                startActivity(logIntentCam)
                finish()
            } else
                Toast.makeText(this, "Credentials invalid.", Toast.LENGTH_SHORT).show()
        }
    }
}

//    fun fjaLog(view: View) {
//        Toast.makeText(this, "Button pressed.", Toast.LENGTH_SHORT).show()
//        if(usernameEntered == "daddy" && passEntered == "123")
//        {
//            Toast.makeText(this, "Stuff happened", Toast.LENGTH_SHORT).show()
//            val logIntentCam = Intent(this, CameraActivity::class.java)
//            startActivity(logIntentCam)
//            finish()
//        }
//        else
//            Toast.makeText(this, "Credentials invalid.", Toast.LENGTH_SHORT).show()
//    }