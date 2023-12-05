package com.example.bookmark.ui


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bookmark.DBClass
import com.example.bookmark.MainActivity
import com.example.bookmark.R
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var imageView: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 1001

    companion object {
        // Agregar un método estático para crear una nueva instancia del fragmento
        fun newInstance(username: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        imageView = view.findViewById(R.id.ivicon)

        imageView.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                abrirCamara()
            } else {
                // Solicitar permiso
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }

        // Obtener el nombre del usuario
        val username = arguments?.getString("username") ?: ""
        val tvuser: TextView = view.findViewById(R.id.tvuser)
        tvuser.text = username

        val btnlogout: Button = view.findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val btndeleteacc: Button = view.findViewById(R.id.btndeleteacc)
        btndeleteacc.setOnClickListener {
            val db = DBClass(requireContext())
            val username = arguments?.getString("username")

            // Eliminar el usuario y sus libros
            if (username != null) {
                db.deleteUserAndBooks(username)
            } else{
                Toast.makeText(context, "Error al eliminar. Vuelva a intentarlo.", Toast.LENGTH_LONG).show()
            }

            // Redirigimos a la pantalla de inicio de sesión
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }


    private fun abrirCamara() {
        val intentoCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentoCamara, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            // El usuario denegó el permiso, puedes manejarlo aquí
            Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            saveImageInternalStorage(imageBitmap)
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun saveImageInternalStorage(bitmap: Bitmap?) {
        val filename = "imagen_capturada.jpg"
        val stream: FileOutputStream
        try {
            stream = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}