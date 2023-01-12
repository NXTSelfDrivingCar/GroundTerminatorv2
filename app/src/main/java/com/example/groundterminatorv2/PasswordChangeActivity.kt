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
//RAZLIKA
        if(currentPass.text.isNotEmpty() && newPass.text.isNotEmpty() && confirmNewPass.text.isNotEmpty() && newPass.text.equals(confirmNewPass))
        {
            val url = URL("http://nxt-its.duckdns.org:5000/user/login/mobile")
            val postData = "currentPassword" + currentPass.text + "&newPassword" + newPass.text
            
        if(true)
//        if(currentPass.text.isNotEmpty() && newPass.text.isNotEmpty() && confirmNewPass.text.isNotEmpty() && newPass.text.equals(confirmNewPass))
        {
            val url = URL("http://192.168.0.23:5000/user/update/mobile")

            val postData = "tkn=" + CurrentUser.token
            //val postData = "currentPassword=" + currentPass.text + "&newPassword=" + newPass.text
//>>>>>>> test

            val conn = url.openConnection()
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())
            var odgovor: JSONObject? = null
            DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
            BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    Log.d("NXT login", line as String) // Ovo ispisuje sta server kaze
                    odgovor= JSONObject(line)
                }
            }
            var status = odgovor!!.get("status").toString()
            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
            if(status == "OK")
            {
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