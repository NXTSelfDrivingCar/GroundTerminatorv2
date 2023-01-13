package com.example.groundterminatorv2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.groundterminatorv2.httpHandler.HTTPHandler
import com.example.groundterminatorv2.shared.CurrentUser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    fun changePassClicked(v:View) {
        val intentPassChange = Intent(this, PasswordChangeActivity::class.java)
        startActivity(intentPassChange)
        finish()
    }

    fun changeUsernameClicked(v:View){
        val intentUsernameChange = Intent(this, UsernameChangeActivity::class.java)
        startActivity(intentUsernameChange)
        finish()
    }

    fun changeEmailClicked(v:View){
        val intentEmailChange = Intent(this, EmailChangeActivity::class.java)
        startActivity(intentEmailChange)
        finish()
    }

    fun deleteAccontClicked(v:View)
    {
        val postData: String= "tkn=" + CurrentUser.token
        var response = HTTPHandler.handlePostMethod("/user/remove/mobile", postData)
        var status = response.content.get("status")
        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()

        if(status == "OK")
        {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        //inflate layout row.xml
//        var myView = layoutInflater.inflate(R.layout.row, null)
//        val myNote = listNotesAdapter[position]
//        myView.titleTv.text = myNote.nodeName
//        myView.descTv.text = myNote.nodeDes
//        //delete button click
//
//        myView.deleteBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(this@CameraActivity)
//            builder.setMessage("Are you sure you want to Delete?")
//                .setCancelable(false)
//                .setPositiveButton("Yes") { dialog, id ->
//                    // Delete selected note from database
//                    var dbManager = DbManager(this.context!!)
//                    val selectionArgs = arrayOf(myNote.nodeID.toString())
//                    dbManager.delete("ID=?", selectionArgs)
//                    LoadQuery("%")
//                }
//                .setNegativeButton("No") { dialog, id ->
//                    // Dismiss the dialog
//                    dialog.dismiss()
//                }
//            val alert = builder.create()
//            alert.show()
//        }
}