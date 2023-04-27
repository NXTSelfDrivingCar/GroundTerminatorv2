package com.example.groundterminatorv2.WSHandler

import android.util.Log
import com.example.groundterminatorv2.shared.CurrentUser
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import kotlinx.coroutines.*

class WSHandler(var addressarg: String) {

    private var Address: String = addressarg
    lateinit var Socket: Socket
    var connected: Boolean = false

    fun connect(): Boolean {
        //if not connected
        if(Socket == null)
        {
            try {
                Socket = IO.socket(Address)
            }
            catch (E: Exception)
            {
                return false
            }
            return true
        }

        if(connected == false)
        {
            try{
                Socket.connect()
            }
            catch(E: Exception)
            {
                return false
            }
        }
        return true
    }

    fun init() = runBlocking {
        Socket.connect()
        if(connect() == true) {
            launch {
                Socket.emit("ping", "ping")
                //start timer
            }
            Socket.on("pong"){connected = true}
        }
        //make a coroutine to constantly check connection
    }

    fun disconnect()
    {
        //if not connected
        //if connected
    }

    fun getAddress(): String
    {
        return Address
    }
    fun setAddress(address: String, port: String) {
        Address = "http://$address:$port"
    }

    fun connectWS() {
        mSoc.connect();
        mSoc.send("Hello wrld.")

        mSoc.on("message") { message ->
            Log.d(
                "message: ",
                "WebSocketConnectionHandler. Message received: " + message[0]
            )
        }

        var params = mapOf("room" to "streamer", "token" to CurrentUser.token)
        var jObject = JSONObject(params)
        mSoc.emit("joinRoom", jObject)

        mSoc.on("nxtControl") {
            Log.d("nxtControl", it[0].toString())
        }
        Log.d("mini", jObject.toString())
    }

    private fun ping()
    {
        mSoc.emit("ping")
    }
}