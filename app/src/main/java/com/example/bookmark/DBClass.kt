package com.example.bookmark

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBClass(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Bookmark"

        // tabla usuarios
        private val TABLE_USER = "user"
        private val KEY_IDUSER = "id_user"
        private val KEY_UNAME = "username"
        private val KEY_EMAIL = "email"
        private val KEY_PSWD = "pswd"

        // tabla libros
        private val TABLE_BOOK = "book"
        private val KEY_IDBOOK = "id_book"
        private val KEY_TITLE = "titulo"
        private val KEY_AUTOR = "autor"
        private val KEY_TOTALPP = "total_pages"
        private val KEY_CURRENTP = "current_page"
        private val KEY_RATE = "rate"
        private val KEY_ANNOTATION = "annotation"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla usuarios
        val usertb = ("CREATE TABLE " + TABLE_USER + "("
                + KEY_IDUSER + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_UNAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PSWD + " TEXT" + ")")
        db?.execSQL(usertb)

        val booktb = ("CREATE TABLE " + TABLE_BOOK + "("
                + KEY_IDBOOK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_UNAME + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_AUTOR + " TEXT,"
                + KEY_TOTALPP + " INTEGER,"
                + KEY_CURRENTP + " INTEGER,"
                + KEY_RATE + " TEXT,"
                + KEY_ANNOTATION + " TEXT,"
                + "FOREING KEY " + KEY_UNAME + " REFERENCES " + TABLE_USER +"("+ KEY_UNAME +")" + ")")
        db?.execSQL(booktb)


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK)
        onCreate(db)
    }

    fun insertBook(book: Book) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_UNAME, book.username)
        values.put(KEY_TITLE, book.title)
        values.put(KEY_AUTOR, book.autor)
        values.put(KEY_TOTALPP, book.totalpp)
        values.put(KEY_CURRENTP, book.currentp)
        values.put(KEY_RATE, book.rate)
        values.put(KEY_ANNOTATION, book.annotation)

        db.insert(TABLE_BOOK, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllBooksByUser(username: String): List<Book> {
        val booksList = mutableListOf<Book>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_BOOK WHERE $KEY_UNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    cursor.getString(cursor.getColumnIndex(KEY_IDBOOK)),
                    cursor.getString(cursor.getColumnIndex(KEY_UNAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndex(KEY_AUTOR)),
                    cursor.getString(cursor.getColumnIndex(KEY_TOTALPP)),
                    cursor.getString(cursor.getColumnIndex(KEY_CURRENTP)),
                    cursor.getString(cursor.getColumnIndex(KEY_RATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_ANNOTATION))
                )
                booksList.add(book)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return booksList
    }



    fun deleteUserAndBooks(username: String) {
        val db = this.writableDatabase
        db.delete(TABLE_USER, "$KEY_UNAME=?", arrayOf(username))
        db.delete(TABLE_BOOK, "$KEY_UNAME=?", arrayOf(username))
        db.close()
    }



    data class User(
        val id_user: String = KEY_IDUSER,
        val email: String  = KEY_EMAIL,
        val username: String = KEY_UNAME,
        val pswd: String = KEY_PSWD
    )

    data class Book(
        val id_book: String = KEY_IDBOOK,
        val username: String = KEY_UNAME,
        val title: String = KEY_TITLE,
        val autor: String = KEY_AUTOR,
        val totalpp: String = KEY_TOTALPP,
        val currentp: String = KEY_CURRENTP,
        val rate: String = KEY_RATE,
        val annotation: String = KEY_ANNOTATION,
    )

}
