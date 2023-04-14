package com.example.groundterminatorv2.bluetoothManager

import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

class NXTBluetoothController {

    private lateinit var bluetoothResolver: BluetoothResolver

    private var bluetoothSocket: BluetoothSocket? = null

    constructor(bluetoothResolver: BluetoothResolver){
        this.bluetoothResolver = bluetoothResolver
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun setSocket(socketName: String){
        if(socketName == ""){
            return
        }

        this.bluetoothSocket = bluetoothResolver.getPairedDeviceByName(socketName)
    }

    /**
     * Runs the array of controls from [NXTCommand.commands]
     * Before running the command, [BluetoothResolver.connectSocket] provides connection with the socket
     * After finishing the command, [BluetoothResolver.disconnectSocket] closes the connection
     *
     * @param nxtCommand Command object to be run
     *
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun runCommand(nxtCommand: NXTCommand){
        bluetoothResolver.connectSocket(bluetoothSocket!!)

        nxtCommand.getCommands().forEach{ command: ByteArray ->

            val header = byteArrayOf(command.size.toByte(), 0x00)
            bluetoothSocket!!.outputStream.write(header, 0, header.size)
            bluetoothSocket!!.outputStream.write(command, 0, command.size)

            val result = bluetoothSocket!!.inputStream.readNBytes(5)

        }

    }

    fun getSocket(): BluetoothSocket?{
        return this.bluetoothSocket
    }

}