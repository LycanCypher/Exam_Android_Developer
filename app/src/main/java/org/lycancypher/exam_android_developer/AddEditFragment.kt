package org.lycancypher.exam_android_developer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.lycancypher.exam_android_developer.databinding.FragmentAddEditBinding
import org.lycancypher.exam_android_developer.room.NewDb
import org.lycancypher.exam_android_developer.room.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddEditFragment : Fragment() {

    private lateinit var binding: FragmentAddEditBinding
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        const val PERMISSION_ID = 33
        const val PERMISSION_CAMERA_ID = 28
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddEditBinding.inflate(layoutInflater)
        val view = binding.root

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        with(binding) {
            btnAddUsr.isEnabled = false

            enableBtnAdd()

            btnAddUsr.setOnClickListener{
                var okCorreo = validaEmail(etMail.text.toString())
                var okTel = validaTel(etTel.text.toString())
                if (okCorreo && okTel) {
                    addUser()
                }
            }

            ivUserPic.setOnClickListener {
                if(checkCameraPermission()){
                    openCamera()
                } else{
                    requestCameraPermissions()
                }
            }
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Agregar o editar usuario"

        getLocation()

        return view
    }

    private fun addUser() {

        val user = User(
            name = binding.etName.text.toString(),
            apPat = binding.etApelPat.text.toString(),
            apMat = binding.etApelMat.text.toString(),
            phone = binding.etTel.text.toString(),
            mail = binding.etMail.text.toString(),
            lat = binding.etLat.text.toString(),
            longitude = binding.etLong.text.toString()
        )

        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        executor.execute(Runnable {
            val userArray = NewDb
                .getInstance(context = requireContext())
                ?.userDao()
                ?.insertUser(user)

            Handler(Looper.getMainLooper()).post(Runnable {
                findNavController().navigate(
                    R.id.action_addEditFragment_to_userListFragment
                )
            })
        })
    }

    private fun enableBtnAdd() {
        with(binding) {
            etName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })

            etApelPat.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
            etApelMat.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
            etTel.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
            etMail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
            etLat.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
            etLong.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    checkFields()
                }
            })
        }
    }

    private fun checkFields() {
        with(binding){
            if (etName.text.toString().isNotEmpty()
                && etApelPat.text.toString().isNotEmpty()
                && etApelMat.text.toString().isNotEmpty()
                && etTel.text.toString().isNotEmpty()
                && etMail.text.toString().isNotEmpty()
                && etLat.text.toString().isNotEmpty()
                && etLong.text.toString().isNotEmpty()
            ) {
                btnAddUsr.isEnabled = true
            }
        }
    }

    private fun validaEmail(email: String): Boolean {
        return if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            true
        }
        else {
            Toast.makeText(context, "Ingresa una dirección de correo válida", Toast.LENGTH_LONG).show()
            binding.etMail.text.clear()
            binding.btnAddUsr.isEnabled = false
            false
        }
    }

    private fun validaTel(tel: String): Boolean {
        return if (tel.length == 10) {
            true
        }
        else {
            Toast.makeText(context, "Ingresa un número de 10 dígitos", Toast.LENGTH_LONG).show()
            binding.etTel.text.clear()
            binding.btnAddUsr.isEnabled = false
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (this.isLocationEnabled()) {
                mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    binding.etLat.setText(it.latitude.toString())
                    binding.etLong.setText(it.longitude.toString())
                }
            }
        }
        else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        return (checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            && checkGranted(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun checkGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera(){
        //val intent = Intent(this, CameraActivity::class.java)
        val intent = Intent (activity, CameraActivity::class.java)
        activity?.startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CAMERA_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera()
            }
        }
    }

    private fun checkCameraPermission(): Boolean{
        return ActivityCompat
            .checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_CAMERA_ID
        )
    }
}
