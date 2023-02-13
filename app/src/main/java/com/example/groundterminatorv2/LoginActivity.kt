package com.example.groundterminatorv2

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
import com.example.groundterminatorv2.shared.CurrentUser
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.httpHandler.HTTPResponse


class LoginActivity : AppCompatActivity() {
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityLogInPage2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//        binding = ActivityLogInPage2Binding.inflate(layoutInflater)
    setContentView(R.layout.activity_login)
//
//        setSupportActionBar(binding.toolbar)

        val policy : StrictMode.ThreadPolicy  = StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy)

    }

    fun logInButton(v: View) {

            if(/*status == "OK"*/true)
            {
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                finish()
            }

    }

    fun registerButton (v: View)
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
