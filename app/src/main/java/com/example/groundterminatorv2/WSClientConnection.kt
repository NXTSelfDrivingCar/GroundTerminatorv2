package com.example.groundterminatorv2

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import io.socket.client.IO
import kotlinx.coroutines.runBlocking
import okhttp3.Request


class WSClientConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wsclient_connection)
    }

    fun onConnectionClick(v: View){

        Log.d("WSConnecion", "Installing http client")

        val mSoc = IO.socket("http://192.168.216.58:5001");
        
        mSoc.connect();

        Log.d("WSConnection", "Connected");

        while (true){
            mSoc.emit("message", "message from and")
        }
    }
}