package com.example.groundterminatorv2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.groundterminatorv2.shared.CurrentUser
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        btnDeleteAccount.setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            intent.putExtra("popuptitle", "Error")
            intent.putExtra("popuptext", "Sorry, that email address is already used!")
            intent.putExtra("popupbtn", "OK")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }

//socket message button
        findViewById<Button>(R.id.btnSocketMessage).setOnClickListener{
            val nmFPS = Math.round((1000 / 30).toDouble())
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    GlobalScope.launch { takePhoto() }
                }
            }, 0, nmFPS)
//            val socMsgIntent = Intent(this, SocketMessageActivity::class.java)
//            startActivity(socMsgIntent)
//            finish()
        }

        // hide the action bar
        supportActionBar?.hide()

//password change button
        findViewById<Button>(R.id.btnPasswordChange).setOnClickListener{
            val intentPassChange = Intent(this, PasswordChangeActivity::class.java)
            startActivity(intentPassChange)
            finish()
        }

// Username change button
        findViewById<Button>(R.id.btnUsernameChange).setOnClickListener{
            val intentUsernameChange = Intent(this, UsernameChangeActivity::class.java)
            startActivity(intentUsernameChange)
            finish()
        }

//Email change
        findViewById<Button>(R.id.btnEmailChange).setOnClickListener{
            val intentEmailChange = Intent(this, EmailChangeActivity::class.java)
            startActivity(intentEmailChange)
            finish()
        }

        val btnDelAccValue = findViewById<Button>(R.id.btnDeleteAccount)
//delete account activity
        btnDelAccValue.setOnClickListener{
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
        findViewById<Button>(R.id.btnCapture).setOnClickListener {
            takePhoto()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    lateinit var photoFile : File
    var counter = 0
    fun takePhoto() {
        val imageCapture = imageCapture ?: return
            // Set up image capture listener,
            // which is triggered after photo has
            // been taken
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageCapturedCallback(){
                    @SuppressLint("UnsafeOptInUsageError")
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                        counter++
                        GlobalScope.launch{
                            val image = imageProxy.image
                            compressSend(imageProxy)
                            Log.d("counter", counter.toString())
                            image!!.close()
                        }
                    }
                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                    }
                }

            )
    }

    suspend fun compressSend(image: ImageProxy){
        val base64String = imageProxyToBase64(image)
        mSoc.emit("stream", base64String)
    }

    @SuppressLint("NewApi")
    private fun imageProxyToBase64(image: ImageProxy): String {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val stream = ByteArrayOutputStream()

        bm.compress(Bitmap.CompressFormat.JPEG, 20, stream)

        val byteFormat = stream.toByteArray()
        val imgString = Base64.getEncoder().encodeToString(byteFormat)

        return imgString
    }

    val mSoc: Socket = IO.socket("http://192.168.1.23:5001");

    fun tvojaMama(v: View){
        Log.d("WSConnection", "Installing http client")
        mSoc.connect();
        Log.d("WSConnection", "Connected");
        mSoc.send("Hello wrld.")

        mSoc.on("message") { message ->
//            mSoc.send("Server is returning the message back: " + message);
            Log.d("mesig: ", "WebSocketConnectionHandler. Message received: " + message[0])
        }

        var params = mapOf("room" to "streamer", "token" to CurrentUser.token)
        var jObject = JSONObject(params)
        mSoc.emit("joinRoom", jObject)
//        mSoc.send(photoFile.readBytes().toString())
        Log.d("mini", jObject.toString())
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
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

    // creates a folder inside internal storage
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
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