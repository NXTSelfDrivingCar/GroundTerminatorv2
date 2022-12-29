package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
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

    private val usernameValue: EditText = findViewById(R.id.etUsername)
    private val emailValue: EditText = findViewById(R.id.etEmail)
    private val passwordValue: EditText = findViewById(R.id.etPassword)
    private val confirmPasswordValue: EditText = findViewById(R.id.etConfirmPassword)
    fun btnRegClicked(v: View) {
        if (usernameValue.text.isNotEmpty() && emailValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()
            && confirmPasswordValue.text.isNotEmpty() && passwordValue.equals(confirmPasswordValue))
        {
            val url = URL("http://192.168.1.101:5000/user/register/mobile")
            val postData = "username=" + usernameValue.text + "&password=" + passwordValue.text + "&password2=" + confirmPasswordValue.text + "&email" + emailValue.text

            val conn = url.openConnection()
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())
            var odgovor: JSONObject? = null
            DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
            BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    Log.d("NXT register", line as String) // Ovo ispisuje sta server kaze
                    odgovor = JSONObject(line)
                }
            }
            var status = odgovor!!.get("status").toString()
            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if (status == "OK") {
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        }
    }
}