package com.dicoding.a31submission4.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.OVERVIEW
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.POSTER
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.RELEASE_DATE
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.TITLE
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.VOTE_AVERAGE
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.ID_TVSHOW
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.TABLEE_NAME
import com.dicoding.a31submission4.model.TvShowFavoriteModel
import java.sql.SQLException

class TvShowFavoriteHelper(context: Context?) {

    companion object {
        private const val DATABASE_TABLE = TABLEE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: TvShowFavoriteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context?): TvShowFavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = TvShowFavoriteHelper(context)
                    }
                }
            }
            return INSTANCE as TvShowFavoriteHelper
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
            "$ID_TVSHOW ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID_TVSHOW = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insertTv(tv: TvShowFavoriteModel?): Long {
        open()
        val args = ContentValues()
        args.put(POSTER, tv?.poster)
        args.put(TITLE, tv?.title)
        args.put(RELEASE_DATE, tv?.release_date)
        args.put(VOTE_AVERAGE, tv?.vote_average)
        args.put(OVERVIEW, tv?.overview)
        return database.insert(DATABASE_TABLE, null, args)
        close()
    }

    fun deleteTv(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID_TVSHOW = '$id'", null)
    }
}