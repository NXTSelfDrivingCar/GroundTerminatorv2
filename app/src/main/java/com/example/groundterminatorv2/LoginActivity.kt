package com.example.groundterminatorv2

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groundterminatorv2.httpHandler.HTTPHandler
<<<<<<< Updated upstream
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import com.example.groundterminatorv2.databinding.ActivityLogInPage2Binding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.DataOutputStream
import java.net.URL
=======
import com.example.groundterminatorv2.httpHandler.HTTPResponse
>>>>>>> Stashed changes
import com.example.groundterminatorv2.shared.CurrentUser
import com.example.groundterminatorv2.httpHandler.HTTPResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    val policy : StrictMode.ThreadPolicy  = StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy)
}

    fun logInButton(v: View) {
        val usernameValue: EditText = findViewById<EditText>(R.id.etEmail)
        val passwordValue: EditText = findViewById<EditText>(R.id.etPassword)

        if (usernameValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()) {

            HTTPHandler.Address = "http://192.168."+etServerAddress.text
            Toast.makeText(this, "${HTTPHandler.Address+":5000"}", Toast.LENGTH_SHORT).show()
            var params = mapOf("username" to usernameValue.text, "password" to passwordValue.text)

            val postData = params.map {(k, v) -> "${(k)}=${v}"}.joinToString("&")

            var response : HTTPResponse? = null

            //attempts to get a response, if no value leave function
            try
            {
                response = HTTPHandler.handlePostMethod("/user/login/mobile", postData)

            } catch (E: Exception){
                return;
            }

            //if response is null function has no values to work with
            if(response==null)
            {
                return
            }

            // Gets login status { OK | Unauthorized }
            var status = response.content.get("status")

            // Gets header cookie { JWT }
            var headerCookie = response.conn.headerFields["set-cookie"]

            Log.d("NXT Login status", status as String)

            var extractedToken: String? = null

            // Handles token, and extracts just authorization part
            if(headerCookie == null) {
                Toast.makeText(this, "Error, invalid token", Toast.LENGTH_SHORT).show()
                return
            }
            //trims current tokens
            for(cookie in headerCookie!!){
                Log.d("NXT Login cookie loop", cookie as String)
                if(cookie.startsWith("auth=")){
                    extractedToken = cookie.split(";")[0].replace("auth=", "")
                }
            }

            if(extractedToken == null) {
                Toast.makeText(this, "Error, invalid token", Toast.LENGTH_SHORT).show()
                return
            }

            Log.d("NXT Login token", extractedToken!!)

            // Setting up the current user of the app
            CurrentUser.token = extractedToken

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if(status == "loginComplete")
            {
                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CameraActivity::class.java)
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
}
