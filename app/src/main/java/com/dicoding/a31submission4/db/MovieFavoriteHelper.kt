package com.dicoding.a31submission4.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.ID_MOVIE
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.OVERVIEW
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.POSTER
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.RELEASE_DATE
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.TABLE_NAME
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.TITLE
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.VOTE_AVERAGE
import com.dicoding.a31submission4.model.MovieModel
import java.sql.SQLException

class MovieFavoriteHelper(context: Context?) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: MovieFavoriteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context?): MovieFavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MovieFavoriteHelper(context)
                    }
                }
            }
            return INSTANCE as MovieFavoriteHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID_MOVIE ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID_MOVIE = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insertMovie(movie: MovieModel?): Long {
        open()
        val args = ContentValues()
        args.put(POSTER, movie?.poster)
        args.put(TITLE, movie?.title)
        args.put(RELEASE_DATE, movie?.release_date)
        args.put(VOTE_AVERAGE, movie?.vote_average)
        args.put(OVERVIEW, movie?.overview)
        return database.insert(DATABASE_TABLE, null, args)
        close()
    }

    fun deleteMovie(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID_MOVIE = '$id'", null)
    }
}