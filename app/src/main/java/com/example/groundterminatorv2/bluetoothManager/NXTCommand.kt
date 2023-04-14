package com.example.groundterminatorv2.bluetoothManager


enum class Motor(var value: Byte) {
    LEFT(0x0),
    RIGHT(0x1),
    BOTH(0xff.toByte())
}

class NXTCommand {

    private lateinit var commands: ArrayList<ByteArray>

    constructor(){
        commands = ArrayList<ByteArray>()
    }

    /**
     * Adds a control to the array of controls
     *
     * @param motor [Motor] (LEFT, RIGHT, BOTH)
     * @param power Intensity of motor rotation
     */
    public fun addControl(motor: Motor, power: Int): NXTCommand {
        var command = byteArrayOf(0x00, 0x4, motor.value, power.toByte(), 0x01, 0x01, 0x64, 0x20, 0, 0, 0,
            0xff.toByte())

        commands.add(command)

        return this
    }

    public fun getCommands(): ArrayList<ByteArray>{
        return this.commands
    }
}