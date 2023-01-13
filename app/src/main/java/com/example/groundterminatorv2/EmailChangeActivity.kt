package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.shared.CurrentUser

class EmailChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_change)
    }

    fun confirmButton(v: View) {
        val newEmail: EditText = findViewById<EditText>(R.id.etNewEmail)
        val postData: String= "type=email&value=" + newEmail.text + "&token=" + CurrentUser.token
        var response = HTTPHandler.handlePostMethod("/user/update/mobile", postData)
        var status = response.content.get("status")
        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()

        if(status == "OK")
        {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}