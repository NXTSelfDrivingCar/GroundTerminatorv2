package com.example.groundterminatorv2


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groundterminatorv2.WSHandler.WSHandler
import com.example.groundterminatorv2.databinding.ActivityLoginBinding
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.httpHandler.HTTPResponse
import com.example.groundterminatorv2.shared.CurrentUser
import io.ktor.client.*

class LoginActivity : AppCompatActivity() {

    //viewBinding implemented, replacing findViewById.
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy : StrictMode.ThreadPolicy  = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Log in button clicked
        binding.btnLogIn.setOnClickListener{
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty() && binding.etServerAddress.text.isNotEmpty()) {
                //have user enter current server address
                HTTPHandler.setAddress("192.168."+binding.etServerAddress.text.toString(), "5000")

                val params = mapOf("username" to binding.etEmail.text, "password" to binding.etPassword.text)
                val postData = params.map {(k, v) -> "${(k)}=${v}"}.joinToString("&")
                val response : HTTPResponse?

                //attempts to get a response, if no value leave function
                try
                {
                    response = HTTPHandler.handlePostMethod("/user/login/mobile", postData)
                } catch (E: Exception){
                    Toast.makeText(this, "catch", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //if response is null function has no values to work with
                if(response==null)
                {
                    return@setOnClickListener
                }

                // Gets login status { OK | Unauthorized }
                var status = response.content.get("status")

                // Gets header cookie { JWT }
                var headerCookie = response.conn.headerFields["Set-Cookie"]

                Log.d("NXT Login status", status as String)

                var extractedToken: String? = null

                if(headerCookie == null) {
                    Toast.makeText(this, "Unable to retrieve cookie", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Handles token, and extracts just authorization part
                //trims current tokens
                for(cookie in headerCookie!!){
                    Log.d("NXT Login cookie loop", cookie as String)
                    if(cookie.startsWith("auth=")){
                        extractedToken = cookie.split(";")[0].replace("auth=", "")
                    }
                }

                if(extractedToken == null) {
                    Toast.makeText(this, "Error, invalid token", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Log.d("NXT Login token", extractedToken!!)

                // Setting up the current user of the app
                CurrentUser.token = extractedToken

                if(status == "loginComplete")
                {
                    WSHandler.setAddress("192.168.${binding.etServerAddress.text.toString()}", "5001")
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
    }
}
