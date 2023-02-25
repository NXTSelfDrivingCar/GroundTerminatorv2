package com.example.groundterminatorv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_socket_message.*

class SocketMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket_message)

        val sendMsgValue = findViewById<Button>(R.id.btnSendMsg)
        val messageValue = (findViewById<EditText>(R.id.etMessage)).text

        sendMsgValue.setOnClickListener{
            val mSoc = IO.socket("http://192.168.1.23:5001");
            Log.d("WSConnection", "Installing http client")
            mSoc.connect();
            if(mSoc.connected())
            {
                Log.d("WSConnection", "Connected");
                mSoc.send(messageValue)
            }
        }
    }
}