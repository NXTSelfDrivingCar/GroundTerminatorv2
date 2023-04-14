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

class EmailChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_change)
    }

    fun confirmButton(v: View) {
        val newEmailValue = findViewById<EditText>(R.id.etNewEmail)
        val passwordValue = findViewById<EditText>(R.id.etPassword)

        if (passwordValue.text.isNotEmpty() && passwordValue.text.isNotEmpty()){

            val postData = "token=" + CurrentUser.token + "&email=" + newEmailValue.text + "&currentPassword=" + passwordValue.text

            var response: HTTPResponse = HTTPHandler.handlePostMethod("/user/update/mobile", postData)

            var status = response.content.get("status").toString()

            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()

            if(status == "updateComplete") {
                val intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Password set.", Toast.LENGTH_SHORT)
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