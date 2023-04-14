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

    fun btnLoginClicked(v: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun btnRegClicked(v: View) {
        val usernameValue: EditText = findViewById(R.id.etUsername)
        val emailValue: EditText = findViewById(R.id.etEmail)
        val passwordValue: EditText = findViewById(R.id.etPassword)
        val confirmPasswordValue: EditText = findViewById(R.id.etConfirmPassword)

        Log.d("prazno", (usernameValue.text.isEmpty() || emailValue.text.isEmpty() || passwordValue.text.isEmpty() || confirmPasswordValue.text.isEmpty()).toString())
        if(usernameValue.text.isEmpty() || emailValue.text.isEmpty() || passwordValue.text.isEmpty() || confirmPasswordValue.text.isEmpty())
        {
            Toast.makeText(this, "Polje je prazno.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("passCheck", (passwordValue.text.toString() == confirmPasswordValue.text.toString()).toString())
        if(!(passwordValue.text.toString().equals(confirmPasswordValue.text.toString())))
        {
            Toast.makeText(this, "Sifre se ne podudaraju.", Toast.LENGTH_SHORT).show()
            return
        }

        val postData = "username=" + usernameValue.text + "&password=" + passwordValue.text + "&email=" + emailValue.text

//        var response = HTTPHandler.handlePostMethod("/user/register/mobile", postData)
        var response = HTTPHandler.handlePostMethod("/user/register/mobile", postData)

        var status = response.content.get("status").toString()
        Log.d("statusCheck", status)



        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        if (status == "registrationComplete") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else
            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
    }
}