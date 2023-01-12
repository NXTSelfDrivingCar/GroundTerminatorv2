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
        val usernameValue: EditText = findViewById<EditText>(R.id.etEmail)
        val passwordValue: EditText = findViewById<EditText>(R.id.etPassword)


        if (usernameValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {
            val postData = "username=" + usernameValue.text + "&password=" + passwordValue.text

            var response = HTTPHandler.handlePostMethod("/user/login/mobile", postData)

            // Gets login status { OK | Unauthorized }
            var status = response.content.get("status")

            // Gets header cookie { JWT }
            var headerCookie = response.conn.headerFields["set-cookie"]

            Log.d("NXT Login status", status as String)

            var extractedToken: String? = null

            // Handles token, and extracts just authorization part
            for(cookie in headerCookie!!){
                Log.d("NXT Login cookie loop", cookie as String)
                if(cookie.startsWith("auth=")){
                    extractedToken = cookie.split(";")[0].replace("auth=", "")
                }
            }

            if(extractedToken == null) {
                return;
            }

            Log.d("NXT Login token", extractedToken!!)

            // Setting up the current user of the app
            CurrentUser.token = extractedToken

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if(status == "OK")
            {
                Toast.makeText(this, "Suck ass", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PasswordChangeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            }
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
