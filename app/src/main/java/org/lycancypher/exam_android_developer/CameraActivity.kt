package org.lycancypher.exam_android_developer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.lycancypher.exam_android_developer.databinding.ActivityCameraBinding
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //ESTO YA NO SE PUDO IMPLEMENTAR

        //startCamera()
    }

    /*private fun startCamera() {
        val preview: Preview = Preview.Builder().apply {
            setTargetResolution(Size(640, 480)) //resolucción
        }.build()



        preview.setOnPreviewOutputUpdateListener {

            // desatamos y volvemos a atar nuestro preview del TextureView
            val parent =  camera_preview.parent as ViewGroup
            parent.removeView(camera_preview)
            parent.addView(camera_preview, 0)

            camera_preview.surfaceTexture = it.surfaceTexture
        }
    }*/
}