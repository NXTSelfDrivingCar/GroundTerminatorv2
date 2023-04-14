package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.shared.CurrentUser
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.httpHandler.HTTPResponse

class PasswordChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)
    }

    fun confirmButton(v: View) {
        val currentPass: EditText = findViewById<EditText>(R.id.etCurrentPass)
        val newPass: EditText = findViewById<EditText>(R.id.etNewPass)
        val confirmNewPass: EditText = findViewById<EditText>(R.id.etPassword)

        if (currentPass.text.isNotEmpty() && newPass.text.isNotEmpty() && confirmNewPass.text.isNotEmpty() && newPass.text.toString().equals(confirmNewPass.text.toString())){

            val postData = "token=" + CurrentUser.token + "&password=" + newPass.text + "&currentPassword=" + currentPass.text

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