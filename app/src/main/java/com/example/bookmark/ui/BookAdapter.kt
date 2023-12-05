package com.example.bookmark.ui

// En tu archivo BookAdapter.kt
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmark.DBClass.Book
import com.example.bookmark.R

class BookAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val totalPagesTextView: TextView = itemView.findViewById(R.id.totalPagesTextView)
        val currentPageTextView: TextView = itemView.findViewById(R.id.currentPageTextView)
        val rateTextView: TextView = itemView.findViewById(R.id.rateTextView)
        val annotationTextView: TextView = itemView.findViewById(R.id.annotationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_card, parent, false)
        return BookViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.autor
        holder.totalPagesTextView.text = "Total Pages: ${book.totalpp}"
        holder.currentPageTextView.text = "Current Page: ${book.currentp}"
        holder.rateTextView.text = "Rate: ${book.rate}"
        holder.annotationTextView.text = "Annotation: ${book.annotation}"
    }

    override fun getItemCount(): Int {
        return books.size
    }
}
