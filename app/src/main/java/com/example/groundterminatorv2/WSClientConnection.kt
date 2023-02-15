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
import kotlinx.coroutines.runBlocking
import okhttp3.Request


class WSClientConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wsclient_connection)
    }

    fun onConnectionClick(v: View){

        Log.d("WSConnection", "Installing http client")
        val client = HttpClient(CIO){
            install(WebSockets){
                pingInterval = 20_000
            }
        }
        Log.d("WSConnection", "Connecting to WS Server")
        runBlocking {
            client.webSocket(method = HttpMethod.Get, host = "192.168.0.23", port = 5001) {
//                Log.d("WSConnection", "Connected to WS Server")
//                while(true) {
//                    val othersMessage = incoming.receive() as? Frame.Text
//                    println(othersMessage?.readText())
//                }
            }
        }
    }
}