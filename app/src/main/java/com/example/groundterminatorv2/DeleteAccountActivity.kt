package com.example.groundterminatorv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.shared.CurrentUser

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        val actionbar = supportActionBar
    }

    fun btnDelClicked(v: View)
    {
        val passwordValue = findViewById<EditText>(R.id.etPassword)
        val tokenValue = CurrentUser.token

        if(passwordValue.text.toString().isNullOrEmpty())
        {
            Toast.makeText(this, "Password not entered.", Toast.LENGTH_SHORT).show()
            return
        }

        var params = mapOf("currentPassword" to passwordValue.text, "token" to tokenValue.toString())

        val postData = params.map {(k, v) -> "${(k)}=${v}"}.joinToString("&")

        var response = HTTPHandler.handlePostMethod("/user/delete/mobile", postData)

        // Gets response { Returning status: userDeleted, userNotFound, userDeleteFailed }
        var status = response.content.get("status")

        if(status.toString() == "userDeleted")
        {
            CurrentUser.token = null
            val userDeletedIntent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "User deleted.", Toast.LENGTH_SHORT).show()
            startActivity(userDeletedIntent)
            finish()
        }
        else if(status.toString() == "userDeleteFailed")
        {
            CurrentUser.token = null
            val userNotFoundIntent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "User deleted.", Toast.LENGTH_SHORT).show()
            startActivity(userNotFoundIntent)
            finish()
        }
        else if(status.toString() == "userNotFound")
        {
            Toast.makeText(this, "Password not in database.", Toast.LENGTH_SHORT).show()
        }


        Log.d("NXT Login status", status as String)
    }
}