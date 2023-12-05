package com.example.bookmark

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmark.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBClass(applicationContext)
        db = dbHelper.writableDatabase

        binding.signupText.setOnClickListener {
            // Cambiar los textos y ocultar caja correo o no
            toggleSignUpViews()
        }

        binding.loginButton.setOnClickListener {
            if (binding.email.visibility == View.VISIBLE) {
                // Crear cuenta
                createAccount()
            } else {
                // Iniciar sesión
                login()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun toggleSignUpViews() {
        if (binding.email.visibility == View.VISIBLE) {
            // Inicio de sesión cambios
            binding.email.visibility = View.GONE
            binding.emailLayout.visibility = View.GONE
            binding.loginText.text = "Iniciar sesión"
            binding.loginButton.text = "Iniciar sesión"
            binding.signupText.text = "¿No tienes cuenta? Crear cuenta."
        } else {
            // Crear cuenta cambios
            binding.email.visibility = View.VISIBLE
            binding.emailLayout.visibility = View.VISIBLE
            binding.loginText.text = "Crear cuenta"
            binding.loginButton.text = "Crear cuenta"
            binding.signupText.text = "¿Tienes cuenta? Iniciar sesión."
        }
    }

    private fun createAccount() {

        // Lógica para crear cuenta
        val email = binding.email.text.toString()
        val username = binding.username.text.toString()
        val pswd = binding.password.text.toString()

        if (username.isNotEmpty() && pswd.isNotEmpty()) {
            // Verificar si el usuario ya existe
            if (checkIfUserExists(username)) {
                showMessage("Ya existe una cuenta con ese nombre de usuario.")
            //verificar si el correo ya existe
            } else if (checkIfEmailExists(email)) {
                showMessage("Ya existe una cuenta con ese correo electrónico.")
            } else {
                val data = ContentValues()
                data.put("email", email)
                data.put("username", username)
                data.put("pswd", pswd)

                val rs: Long = db.insert("user", null, data)
                if (rs != -1L) {
                    // Éxito al crear la cuenta
                    showMessage("Se ha registrado correctamente")
                    clearFields()
                } else {
                    // Error al crear la cuenta
                    showMessage("No se ha podido crear cuenta. Por favor, inténtalo de nuevo.")
                }
            }
        } else {
            showMessage("Faltan datos por rellenar")
        }
    }

    @SuppressLint("Range")
    private fun login() {
        // Lógica para iniciar sesión
        val username = binding.username.text.toString()
        val pswd = binding.password.text.toString()
        val query = "SELECT * FROM user WHERE username='$username' AND pswd='$pswd'"
        val rs = db.rawQuery(query, null)

        if (rs.moveToFirst()) {
            // Éxito al iniciar sesión
            val username = rs.getString(rs.getColumnIndex("username"))
            rs.close()
            startActivity(Intent(this, Menu::class.java).putExtra("username", username))
        } else {
            // Error al iniciar sesión
            showMessage("Usuario o contraseña incorrectas")
        }
    }

    private fun checkIfUserExists(username: String): Boolean {
        val query = "SELECT username FROM user WHERE username='$username'"
        val rs = db.rawQuery(query, null)
        val exists = rs.moveToFirst()
        rs.close()
        return exists
    }

    private fun checkIfEmailExists(email: String): Boolean {
        val query = "SELECT email FROM user WHERE email='$email'"
        val rs = db.rawQuery(query, null)
        val exists = rs.moveToFirst()
        rs.close()
        return exists
    }

    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Mensaje")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Ok", null)
        alertDialog.show()
    }

    private fun clearFields() {
        binding.email.text?.clear()
        binding.username.text?.clear()
        binding.password.text?.clear()
    }
}
