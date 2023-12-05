package com.example.bookmark.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookmark.DBClass
import com.example.bookmark.R


class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val username = arguments?.getString("username") ?: ""

        val buttonSave = view.findViewById<Button>(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveBook(username)
        }

        return view
    }


    private fun saveBook(username: String?) {
        val title = view?.findViewById<EditText>(R.id.editTextTitle)?.text.toString()
        val author = view?.findViewById<EditText>(R.id.editTextAuthor)?.text.toString()
        val totalPages = view?.findViewById<EditText>(R.id.editTextTotalPages)?.text.toString()
        val currentPage = view?.findViewById<EditText>(R.id.editTextCurrentPage)?.text.toString()
        val rate = view?.findViewById<EditText>(R.id.editTextRate)?.text.toString()
        val annotation = view?.findViewById<EditText>(R.id.editTextAnnotation)?.text.toString()

        if (title.isNotEmpty() && author.isNotEmpty() && totalPages.isNotEmpty()
            && currentPage.isNotEmpty() && rate.isNotEmpty() && annotation.isNotEmpty()
        ) {
            val db = DBClass(requireContext())
            // Create a Book object
            val book = DBClass.Book(
                username = username ?: "",
                title = title,
                autor = author,
                totalpp = totalPages,
                currentp = currentPage,
                rate = rate,
                annotation = annotation
            )
            // Insert the book into the database
            db.insertBook(book)
            // Show a Toast indicating success
            view?.let { showToast("Libro insertado correctamente", it.context) }
            clearFields()
        } else {
            // Show a Toast indicating an error
            view?.let { showToast("Error: Todos los campos son obligatorios", it.context) }
        }
    }

    private fun clearFields() {
        // Clear all the EditText fields
        view?.findViewById<EditText>(R.id.editTextTitle)?.setText("")
        view?.findViewById<EditText>(R.id.editTextAuthor)?.setText("")
        view?.findViewById<EditText>(R.id.editTextTotalPages)?.setText("")
        view?.findViewById<EditText>(R.id.editTextCurrentPage)?.setText("")
        view?.findViewById<EditText>(R.id.editTextRate)?.setText("")
        view?.findViewById<EditText>(R.id.editTextAnnotation)?.setText("")
    }

    // Function to show a Toast with a long duration
    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    companion object {
        // Agregar un método estático para crear una nueva instancia del fragmento
        fun newInstance(username: String): AddFragment {
            val fragment = AddFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }
}
