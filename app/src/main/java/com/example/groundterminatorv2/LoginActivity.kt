package com.example.groundterminatorv2

import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import com.example.groundterminatorv2.databinding.ActivityLogInPage2Binding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.DataOutputStream
import java.net.URL


class LoginActivity : AppCompatActivity() {
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityLogInPage2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//        binding = ActivityLogInPage2Binding.inflate(layoutInflater)
    setContentView(R.layout.activity_main)
//
//        setSupportActionBar(binding.toolbar)

        val policy : StrictMode.ThreadPolicy  = StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy)

    }

    fun logInButton(v: View) {
        val usernameValue: EditText = findViewById<EditText>(R.id.etEmail)
        val passwordValue: EditText = findViewById<EditText>(R.id.etPassword)


        if (!usernameValue.text.isEmpty() && !passwordValue.text.isEmpty()) {
            val url = URL("http://192.168.1.101:5000/user/login/mobile")
            val postData = "username=" + usernameValue.text + "&password=" + passwordValue.text

            val conn = url.openConnection()
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())
            var odgovor: JSONObject? = null
            DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
            BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    Log.d("NXT login", line as String) // Ovo ispisuje sta server kaze
                    odgovor=JSONObject(line)
                }
            }
            var status = odgovor!!.get("status").toString()
            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if(status == "OK")
            {
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        }
    }

    fun RegisterButton (v: View)
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
    //убићу се јебено
}
