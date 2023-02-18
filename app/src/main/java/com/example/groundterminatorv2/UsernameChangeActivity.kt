package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.httpHandler.HTTPResponse
import com.example.groundterminatorv2.shared.CurrentUser

class UsernameChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username_change)
    }

    fun confirmButton(v: View) {
        val currentPass: EditText = findViewById<EditText>(R.id.etCurrentPass)
        val newUsername: EditText = findViewById<EditText>(R.id.etNewUsername)

        if (currentPass.text.isNotEmpty() && newUsername.text.isNotEmpty()){

            val postData = "token=" + CurrentUser.token + "&username=" + newUsername.text + "&currentPassword=" + currentPass.text

            var response: HTTPResponse = HTTPHandler.handlePostMethod("/user/update/mobile", postData)

            var status = response.content.get("status").toString()

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()

            if(status == "updateComplete") {
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Username changed", Toast.LENGTH_SHORT)
                finish()
            }
            else if (status == "userNotFound") {
                Toast.makeText(this, "Password not in database.", Toast.LENGTH_SHORT).show()
            }
            else if(status == "updatefailed") {
                Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            }
        }
    }
}