package com.example.bookmark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmark.DBClass
import com.example.bookmark.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val username = arguments?.getString("username") ?: ""

        // Load books and display them in a RecyclerView using a LinearLayoutManager
        val booksRecyclerView = view.findViewById<RecyclerView>(R.id.booksRecyclerView)
        val books = DBClass(requireContext()).getAllBooksByUser(username)
        val bookAdapter = BookAdapter(books)
        booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        booksRecyclerView.adapter = bookAdapter

        return view
    }

    companion object {
        // Agregar un método estático para crear una nueva instancia del fragmento
        fun newInstance(username: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }

}