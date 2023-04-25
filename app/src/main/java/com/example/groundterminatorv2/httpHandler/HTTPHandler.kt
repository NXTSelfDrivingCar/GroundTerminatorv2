package com.example.groundterminatorv2.httpHandler

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.DataOutputStream
import java.net.URL
import java.net.URLConnection

class HTTPResponse(var conn: URLConnection, var content: JSONObject) {

}

class HTTPHandler {
    companion object{
        private var Address: String = "http://192.168.1.23"

        fun setAddress(address: String, port: String)
        {
            Address= "http://$address:$port"
        }
        fun getAddress(): String
        {
            return Address
        }

        fun handlePostMethod(route: String, postData: String): HTTPResponse{
            val tmpRoute = resolveRoute(route)

            Log.d("HTTP Get request at: ", tmpRoute)

            val url = URL(tmpRoute)

            val conn = url.openConnection()

            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postData.length.toString())

            var response: JSONObject? = null

            DataOutputStream(conn.getOutputStream()).use { it.writeBytes(postData) }
            BufferedReader(InputStreamReader(conn.getInputStream())).use { bf ->
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    Log.d("HTTP Get request", line as String)
                    response=JSONObject(line)
                }
            }

            return HTTPResponse(conn, response!!)
        }

        fun postMethod(route: String, postData: String): JSONObject?{
            return null
        }

        // Resolves routes if starting without '/'
        // user/login -> /user/login
        private fun resolveRoute(route: String): String{
            if(!route.startsWith("/")){
                return "$Address/$route"
            }
            return "$Address$route"
        }
    }
}