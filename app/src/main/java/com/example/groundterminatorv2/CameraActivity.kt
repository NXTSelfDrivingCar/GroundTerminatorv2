package com.example.groundterminatorv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val url = URL("http://localhost:5000")

        with(url.openConnection() as HttpURLConnection)
        {
            requestMethod = "GET"
            println("\n Sent 'GET' request to URL : $url; Response Code : $responseCode")

//            inputStream.bufferedReader().use {
//                it.lines().forEach { line ->
//                    println(line)
//                }
//            }
        }



//        val connection = url.openConnection()
//        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
//            var line: String?
//            while (inp.readLine().also { line = it } != null) {
//                println(line)
//            }
//        }
    }


}