package com.example.groundterminatorv2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.groundterminatorv2.CameraActivity
import com.example.groundterminatorv2.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logClicked = findViewById<Button>(R.id.btnlog)
        val usernameEntered = (findViewById<AppCompatEditText>(R.id.usernameField)).text.toString()
        val passEntered = (findViewById<AppCompatEditText>(R.id.passwordField)).text.toString()

        logClicked.setOnClickListener(View.OnClickListener() {
            fun onClick()
            {
                if(usernameEntered == "daddy" && passEntered == "123")
                {
                    val logIntentCam = Intent(this, CameraActivity::class.java)
                    startActivity(logIntentCam)
                    finish()
                }
                else
                    Toast.makeText(this, "Credentials invalid.", Toast.LENGTH_SHORT).show()
            }
        })

    }
}