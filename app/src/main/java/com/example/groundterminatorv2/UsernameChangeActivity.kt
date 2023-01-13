package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.shared.CurrentUser

class UsernameChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username_change)
    }

    fun confirmButton(v: View) {
        val newUsername: EditText = findViewById<EditText>(R.id.etNewUsername)
        val postData: String= "type=username&value=" + newUsername.text + "&token=" + CurrentUser.token
        var response = HTTPHandler.handlePostMethod("/user/update/mobile", postData)
        var status = response.content.get("status")
        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()

        if(status == "OK")
        {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}