package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }


    fun btnRegClicked(v: View) {
        val usernameValue: EditText = findViewById(R.id.etUsername)
        val emailValue: EditText = findViewById(R.id.etEmail)
        val passwordValue: EditText = findViewById(R.id.etPassword)
        val confirmPasswordValue: EditText = findViewById(R.id.etConfirmPassword)

        // SREDITI OVO OVDE DA MU MAMU NE BIH JEBAO ZNACI NE RADI NISTA KAKO TRBEA
        if(true)
//        if (usernameValue.text.isNotEmpty() && emailValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()
//            && confirmPasswordValue.text.isNotEmpty() && passwordValue.equals(confirmPasswordValue.text.toString()))
        {
//            val url = URL("http://192.168.1.101:5000/user/register/mobile")
            val postData = "username=" + usernameValue.text + "&password=" + passwordValue.text + "&email=" + emailValue.text

            var response = HTTPHandler.handlePostMethod("/user/register/mobile", postData)

            var status = response.content.get("status").toString()

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if (status == "OK") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        }
    }}