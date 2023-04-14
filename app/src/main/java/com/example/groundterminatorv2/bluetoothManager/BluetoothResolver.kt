package com.example.groundterminatorv2.bluetoothManager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat

// This class is a singleton
class BluetoothResolver {

    private var context: Context? = null
    private var activity: Activity? = null

    companion object{
        @Volatile
        private lateinit var instance: BluetoothResolver

        @Volatile
        private lateinit var bluetoothManager: BluetoothManager

        @Volatile
        private var bluetoothAdapter: BluetoothAdapter? = null

        // This function returns a new instance of this class
        // if the instance has not been initialized already
        fun getInstance(): BluetoothResolver {
            synchronized(this){
                if(!Companion::instance.isInitialized){
                    instance = BluetoothResolver()
                }
                return instance
            }
        }
    }

    /**
     *
     * Initializes BluetoothResolver class and asks for permissions.
     * Checks for required Bluetooth permissions, and grants them if not already.
     *
     * Initializes BluetoothManager class
     * Initializes BluetoothAdapter class
     *
     *
     * @see BluetoothManager
     * @see BluetoothAdapter
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    fun init(activity: Activity){
        this.context = activity
        this.activity = activity

        Log.d("BTInit", "Initializing BluetoothResolver")

        // Grants permissions if not granted already
        if(!isPermissionGranted(this.context!!)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val permissions = mutableSetOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
                }

                ActivityCompat.requestPermissions(
                    this.activity!!, permissions.toTypedArray(), 600
                )
            }
        }

        initBluetoothManger()
        initBluetoothAdapter()
    }

    /**
     * Initializes Bluetooth adapter. This function can be used individually, but it is called in
     * BluetoothResolver.init()
     * @throws NullPointerException if Bluetooth manager has not been initialized
     * @throws NullPointerException if activity has not been initialized
     */
    fun initBluetoothAdapter(){
        if(bluetoothManager == null){
            throw java.lang.NullPointerException("Bluetooth manager has not been initialized.\nInitialize manually or with BluetoothResolver.initBluetoothManager()")
        }

        if(activity == null){
            throw java.lang.NullPointerException("Activity has not been initialized.\nInitialize it with BluetoothResolver.init(activity: Activity)")
        }

        bluetoothAdapter = this.getBluetoothManager().adapter

        // Enables Bluetooth if not enabled already
        /*if(bluetoothAdapter != null){
            if(bluetoothAdapter?.isEnabled == false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                //startActivityForResult(this.activity!!, enableBtIntent, 1, null)
                startActivityForResult(activity!!, enableBtIntent, 1, null)
            }
        }*/
    }

    /**
     * Returns the BluetoothSocket by its name.
     * This function goes through paired devices
     *
     * @param name Name of the socket
     * @return Socket if the paired device has the requested name or null
     *
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun getPairedDeviceByName(name: String): BluetoothSocket?{
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        pairedDevices?.forEach{device ->
            if(device.name == name){
                return device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
            }
        }

        return null
    }

    /**
     * Connects the socket if not connected already
     *
     * @param bluetoothSocket Socket to be connected
     * @see BluetoothSocket
     * @see BluetoothDevice
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun connectSocket(bluetoothSocket: BluetoothSocket){
        if(!bluetoothSocket!!.isConnected){
            bluetoothSocket!!.connect()
        }
    }

    /**
     * Disconnects the socket if connected
     *
     * @param bluetoothSocket Socket to be disconnected
     *
     * @see BluetoothSocket
     * @see BluetoothDevice
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun disconnectSocket(bluetoothSocket: BluetoothSocket){
        if(bluetoothSocket!!.isConnected){
            bluetoothSocket!!.close()
        }
    }

    fun getBluetoothAdapter(): BluetoothAdapter?{
        return bluetoothAdapter
    }

    private fun getBluetoothManager(): BluetoothManager {
        return bluetoothManager
    }

    // ***************************************** PRIVATE FUNCTIONS ***************************************** //

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initBluetoothManger(){
        if(this.context == null){
            throw Exception("Context has not been initialized. Context cannot be null.")
        }

        bluetoothManager = context!!.getSystemService<BluetoothManager>(BluetoothManager::class.java)
    }

    // Checks if the permissions were granted by the user
    private fun isPermissionGranted(context: Context): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Prevents from using the constructor
    private constructor()
}