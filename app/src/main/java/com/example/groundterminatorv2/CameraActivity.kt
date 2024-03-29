package com.example.groundterminatorv2

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback

import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ImplementationMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.groundterminatorv2.databinding.ActivityCameraBinding
import com.example.groundterminatorv2.httpHandler.HTTPHandler

import com.example.groundterminatorv2.bluetoothManager.BluetoothResolver
import com.example.groundterminatorv2.bluetoothManager.Motor
import com.example.groundterminatorv2.bluetoothManager.NXTBluetoothController
import com.example.groundterminatorv2.bluetoothManager.NXTCommand
import com.example.groundterminatorv2.shared.CurrentUser
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var webSocketHandler: WSHandler


    private lateinit var binding: ActivityCameraBinding

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "test1")
        setContentView(R.layout.activity_camera)
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

<<<<<<< Updated upstream
=======


        //instances class and calls connect method
        webSocketHandler = WSHandler("http://192.168.1.23:5001")
        webSocketHandler.init()
>>>>>>> Stashed changes

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnDeleteAccount.setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            intent.putExtra("popuptitle", "Error")
            intent.putExtra("popuptext", "Sorry, that email address is already used!")
            intent.putExtra("popupbtn", "OK")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)


//socket message button
        binding.btnSocketMessage.setOnClickListener{

        }


        // hide the action bar
        supportActionBar?.hide()
        //
        //mSoc.on("nxtControl", )


//password change button
        binding.btnPasswordChange.setOnClickListener{
            val intentPassChange = Intent(this, PasswordChangeActivity::class.java)
            startActivity(intentPassChange)
            finish()
        }

// Username change button
        binding.btnUsernameChange.setOnClickListener{
            val intentUsernameChange = Intent(this, UsernameChangeActivity::class.java)
            startActivity(intentUsernameChange)
            finish()
        }

//Email change
        binding.btnEmailChange.setOnClickListener{
            val intentEmailChange = Intent(this, EmailChangeActivity::class.java)
            startActivity(intentEmailChange)
            finish()
        }

//delete account activity
        binding.btnDeleteAccount.setOnClickListener{
            val intentDeleteAccountActivity = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intentDeleteAccountActivity)
            finish()
        }

        // Check camera permissions if all permission granted
        // start camera else ask for the permission
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // set on click listener for the button of capture photo
        // it calls a method which is implemented below

        binding.btnCapture.setOnClickListener {
            //todo give functionality

        findViewById<Button>(R.id.btnCapture).setOnClickListener {

            val nmFPS = Math.round((1000 / 10).toDouble())
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    GlobalScope.launch { compressSend() }
                }
            }, 0, nmFPS)
        }
        cameraExecutor = Executors.newSingleThreadExecutor()

        //BT stvari
        val bluetoothResolver: BluetoothResolver = BluetoothResolver.getInstance()
        bluetoothResolver.init(this)


    suspend fun compressSend(image: ImageProxy){
        val base64String = imageProxyToBase64(image)

        var nxtController: NXTBluetoothController = NXTBluetoothController(bluetoothResolver)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        nxtController.setSocket("NXT")

        mSoc.on("NXTControl") { message ->
            var command = NXTCommand()

            when(message[0]) {
                "up" -> {
                    command.addControl(Motor.BOTH, 100)
                    nxtController.runCommand(command)
                    Thread.sleep(100)
                    command.stop()
                    nxtController.runCommand(command)
                }
                "down" -> {
                    command.addControl(Motor.BOTH, -100)
                    nxtController.runCommand(command)
                    Thread.sleep(100)
                    command.stop()
                    nxtController.runCommand(command)
                }
                "left" -> {
                    command.addControl(Motor.LEFT, 100)
                    nxtController.runCommand(command)
                    command.addControl(Motor.RIGHT, -100)
                    nxtController.runCommand(command)
                    Thread.sleep(100)
                    command.stop()
                    nxtController.runCommand(command)
                }
                "right" -> {
                    command.addControl(Motor.RIGHT, 100)
                    nxtController.runCommand(command)
                    command.addControl(Motor.LEFT, -100)
                    nxtController.runCommand(command)
                    Thread.sleep(100)
                    command.stop()
                    nxtController.runCommand(command)
                }
            }

        }
    }

    fun compressSend(){
        val base64String = imageProxyToBase64()
<<<<<<< Updated upstream

        mSoc.emit("stream", base64String)
=======
//        socket.emit("stream", base64String)
>>>>>>> Stashed changes
    }

    @SuppressLint("NewApi")
    private fun imageProxyToBase64(): String {
        val source = findViewById<PreviewView>(R.id.viewFinder)

        val stream = ByteArrayOutputStream()

        var matrix = android.graphics.Matrix()
        matrix.postRotate(270f)
        val bp = Bitmap.createBitmap(source.bitmap!!, 0, 0, source.bitmap!!.width, source.bitmap!!.height, matrix, true)

        bp.compress(Bitmap.CompressFormat.JPEG, 20, stream)
        val bytes = stream.toByteArray()
        val imgString = Base64.getEncoder().encodeToString(bytes)

        return imgString
    }

<<<<<<< Updated upstream

    val mSoc: Socket = IO.socket(HTTPHandler.Address+":5001");

    fun connectWS(v: View){
        Log.d("WSConnection", "Installing http client")
        mSoc.connect();
        Log.d("WSConnection", "Connected");
        mSoc.send("Hello wrld.")

        mSoc.on("message") { message ->
            Log.d("mesig: ", "WebSocketConnectionHandler. Message received: " + message[0])
        }

        var params = mapOf("room" to "streamer", "token" to CurrentUser.token)
        var jObject = JSONObject(params)
        mSoc.emit("joinRoom", jObject)

        mSoc.on("nxtControl"){
            Log.d("nxtControl", it[0].toString())
        }
        Log.d("mini", jObject.toString())
    }



=======
>>>>>>> Stashed changes
    @SuppressLint("RestrictedApi")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val viewFinderValue = binding.viewFinder


        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            var preview = Preview.Builder()
                .setTargetResolution(Size(320, 240))
                .setMaxResolution(Size(320, 240))
                .build()
                .also {
                    it.setSurfaceProvider(viewFinderValue.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .setDefaultResolution(Size(320, 240))
                .setMaxResolution(Size(320, 240))
                .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()


            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // checks the camera permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // If all permissions granted , then start Camera
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // If permissions are not granted,
                // present a toast to notify the user that
                // the permissions were not granted.
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXGFG"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}