package com.example.groundterminatorv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CameraActivity : AppCompatActivity() {

    private val btnGoBack = findViewById<Button>(R.id.btnGoBack)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        btnGoBack.setOnClickListener {
            finish()
        }
    }
}