package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.shared.CurrentUser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.httpHandler.HTTPResponse

class PasswordChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)
    }

    fun confirmButton(v: View)
    {
        val currentPass : EditText = findViewById<EditText>(R.id.etCurrentPass)
        val newPass : EditText = findViewById<EditText>(R.id.etNewPass)
        val confirmNewPass : EditText = findViewById<EditText>(R.id.etConfirmNewPass)

      if(currentPass.text.isNotEmpty() && newPass.text.isNotEmpty() && confirmNewPass.text.isNotEmpty() && newPass.text.equals(confirmNewPass.text))
      {
            val url = URL("http://nxt-its.duckdns.org:5000/user/login/mobile")
            val postData = "currentPassword" + currentPass.text + "&newPassword" + newPass.text
            
        if(true)
        {
            val postData = "token=" + CurrentUser.token + "&newPassword=" + newPass.text + "&currPassword=" + currentPass.text

            var response: HTTPResponse = HTTPHandler.handlePostMethod("/user/update/mobile", postData)

            var status = response.content.get("status").toString()

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if(status == "OK")
            {
                // Promeni intent
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(status == "PassIncorrect")
            {
                Toast.makeText(this, "Current password is incorrect.", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        }
    }
}