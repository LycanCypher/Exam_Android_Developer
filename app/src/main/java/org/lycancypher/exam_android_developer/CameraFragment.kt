package org.lycancypher.exam_android_developer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.lycancypher.exam_android_developer.databinding.FragmentCameraBinding
import org.lycancypher.exam_android_developer.room.User
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCameraBinding.inflate(layoutInflater)
        val view = binding.root

        outputDirectory = getOutputDirectory()

        if (allPermissionsGranted()) {
            startCamera()
        }
        else {
            requestPermission()
        }

        binding.btnCapture.setOnClickListener {
            takePhoto()
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Tomar foto"

        return view
    }

    private fun getOutputDirectory (): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().applicationContext.filesDir
    }

    private fun takePhoto () {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(MainActivity.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile).toString()
                    val msg = "Foto guardada exitosamente"

                    /*Toast.makeText(activity,
                        "$msg $savedUri",
                        Toast.LENGTH_LONG).show()*/

                    val bundle = bundleOf("usrPic" to savedUri)

                    findNavController().navigate(
                        R.id.action_cameraFragment_to_addEditFragment, bundle
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(
                        MainActivity.TAG,
                        "Error: ${exception.message}",
                        exception
                    )
                }

            }
        )
    }

    private fun startCamera () {

        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.txvCameraView.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.d(MainActivity.TAG, getString(R.string.camera_fail), e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestPermission () {
        ActivityCompat.requestPermissions(requireActivity(), MainActivity.REQUIRED_PERMISSIONS,
            MainActivity.PERMISSION_CAMERA_ID
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == MainActivity.PERMISSION_CAMERA_ID) {
            if (allPermissionsGranted()) {
                startCamera()
            }
            else {
                Toast.makeText(activity,
                    "No tienes los permisos",
                    Toast.LENGTH_LONG).show()

                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted () =
        MainActivity.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(), it
            ) == PackageManager.PERMISSION_GRANTED
        }
}