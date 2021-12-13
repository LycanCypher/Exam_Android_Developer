package org.lycancypher.exam_android_developer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object{
        const val PERMISSION_ID = 33
        const val PERMISSION_CAMERA_ID = 28
        const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val TAG = "CameraX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed () {
        Toast.makeText(this, "No es posible regresar", Toast.LENGTH_LONG).show()
    }
}