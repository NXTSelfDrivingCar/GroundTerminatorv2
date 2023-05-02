package com.example.groundterminatorv2.WSHandler

class WSHandler {
    companion object{
        private lateinit var Address: String

        fun getAddress(): String
        {
            return Address
        }

        fun setAddress(address: String, port: String)
        {
            Address = "http://$address:$port"
        }
    }
}