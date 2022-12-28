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
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = HttpClient(CIO){
            install(ContentNegotiation){

            }
        }
        val btnlog = findViewById<Button>(R.id.btnlog)
        val usernameField = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passField = findViewById<EditText>(R.id.editTextTextPassword)
        btnlog.setOnClickListener {
            Toast.makeText(this, "Button pressed.", Toast.LENGTH_SHORT).show()
            println("ABCD ${passField.text}  ${usernameField.text}")

//            val url = URL("http://localhost:5000")
//            with(url.openConnection() as HttpURLConnection)
//            {
//                requestMethod = "GET"
//                println("\n Sent 'GET' request to URL : $url; Response Code : $responseCode")
//            if ( green light servera ) {
//                val logIntentCam = Intent(this, CameraActivity::class.java)
//                startActivity(logIntentCam)
//                finish()
//            } else
//                Toast.makeText(this, "Credentials invalid.", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    @Serializable
    data class logInfo(val un: String, val pw: String)
}