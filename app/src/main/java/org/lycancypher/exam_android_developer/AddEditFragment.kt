package org.lycancypher.exam_android_developer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
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
    private var lat: String = ""
    private var long: String = ""
    private var usrPic: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAddEditBinding.inflate(layoutInflater)

        arguments?.let { bundle ->
            usrPic = bundle.getString("usrPic", "")
        }
        if (usrPic != "") {
//            Toast.makeText(activity, "El bundle es: $usrPic", Toast.LENGTH_LONG).show()
            with(binding) {
                ivUserPic.setImageURI(usrPic.toUri())
                etName.isEnabled = true
                etApelPat.isEnabled = true
                etApelMat.isEnabled = true
                etTel.isEnabled = true
                etMail.isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //binding = FragmentAddEditBinding.inflate(layoutInflater)
        val view = binding.root

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getLocation()

        with(binding) {
            btnAddUsr.isEnabled = false

            enableBtnAdd()

            btnAddUsr.setOnClickListener{
                val okCorreo = validaEmail(etMail.text.toString())
                val okTel = validaTel(etTel.text.toString())
                if (okCorreo && okTel) {
                    addUser()
                }
            }

            ivUserPic.setOnClickListener {
                openCamera()
            }
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Agregar o editar usuario"

        return view
    }

    private fun addUser() {

        val user = User(
            name = binding.etName.text.toString(),
            apPat = binding.etApelPat.text.toString(),
            apMat = binding.etApelMat.text.toString(),
            phone = binding.etTel.text.toString(),
            mail = binding.etMail.text.toString(),
            lat = lat,
            longitude = long,
            usrPic = usrPic
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

    private fun enableBtnAdd () {
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
        }
    }

    private fun checkFields() {
        with(binding){
            if (etName.text.toString().isNotEmpty()
                && etApelPat.text.toString().isNotEmpty()
                && etApelMat.text.toString().isNotEmpty()
                && etTel.text.toString().isNotEmpty()
                && etMail.text.toString().isNotEmpty()
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
            Toast.makeText(context, getString(R.string.input_ok_mail), Toast.LENGTH_LONG).show()
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
            Toast.makeText(context, getString(R.string.input_ok_phone), Toast.LENGTH_LONG).show()
            binding.etTel.text.clear()
            binding.btnAddUsr.isEnabled = false
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    lat =  it.latitude.toString()
                    long = it.longitude.toString()
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
            MainActivity.PERMISSION_ID
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
        findNavController().navigate(
            R.id.action_addEditFragment_to_cameraFragment
        )

    }
}
