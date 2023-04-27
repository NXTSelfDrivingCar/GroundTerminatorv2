package com.example.groundterminatorv2.WSHandler

import android.util.Log
import com.example.groundterminatorv2.shared.CurrentUser
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import kotlinx.coroutines.*
import java.util.*

class WSHandler(var addressarg: String) {

    private var address: String = addressarg
    var socket: Socket = IO.socket(address)
    var connected: Boolean = false



    fun connect(): Boolean {
        if (!connected) {
            try {
                socket.connect()
                connected = true
                authorize()
            } catch (E: Exception) {
                return false
            }
        }
        return true
    }

    fun init() = runBlocking {
        pingingConst()
    }

    private fun pingingConst()
    {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                var acknowledge = false
                Log.d("pingCheck", "pinging...")
                socket.emit("ping", "ping", Ack {
                    //receives status of connection
                        data ->
                    connected = true
                    acknowledge = true
                    val jData = JSONObject(data[0].toString())
                    Log.d("pingCheck", "during ping ${jData.get("time")}")
                })
                Thread.sleep(1000)
                if(!acknowledge)
                {
                    Log.d("pingCheck", "Not connected")
                    disconnect()
                    Log.d("pingCheck", "Disconnected")
                    connect()
                    Log.d("pingCheck", "Connected")

                }
                else
                {
                    Log.d("pingCheck", "Connection acknowledged.")
                }
            }
        }, 5000, 5000)
    }

    fun disconnect() {
        if(connected){
            socket.disconnect()
            connected = false
            Log.d("ping", "Disconnected.")
        }
        else{
            Log.d("ping", "Already disconnected.")
        }
    }

    fun getAddress(): String {
        return address
    }

    fun setAddress(address: String, port: String) {
        this.address = "http://$address:$port"
    }

    fun connectWS() {
        socket.connect();
        socket.send("Hello wrld.")

        socket.on("message") { message ->
            Log.d(
                "message: ",
                "WebSocketConnectionHandler. Message received: " + message[0]
            )
        }

        var params = mapOf("room" to "streamer", "token" to CurrentUser.token)
        var jObject = JSONObject(params)
        socket.emit("joinRoom", jObject)

        socket.on("nxtControl") {
            Log.d("nxtControl", it[0].toString())
        }
        Log.d("mini", jObject.toString())
    }

    private fun ping()
    {
        var acknowledge = false
        socket.emit("ping", "ping", Ack{
            //receives status of connection
                data -> connected = true
            acknowledge = true
            val jData = JSONObject(data[0].toString())
            Log.d("pingCheck", "during ping ${jData.get("time")}")
        })
    }

    private fun authorize()
    {
        var params = mapOf("room" to "streamer", "token" to CurrentUser.token)
        var jObject = JSONObject(params)
        try {
            socket.emit("joinRoom", jObject)
        }catch (E: Exception)
        {
            return
        }

    }
}