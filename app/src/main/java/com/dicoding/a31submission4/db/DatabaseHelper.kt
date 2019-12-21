package com.dicoding.a31submission4.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.a31submission4.db.DatabaseContract.MovieColumns.Companion.TABLE_NAME
import com.dicoding.a31submission4.db.DatabaseContract.TvShowColumns.Companion.TABLEE_NAME

internal class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_dicodingggggggggggggggggggggggggggggggggggggggggg"
        private const val DATABASE_VERSION = 1

        const val SQL_CREATE_TABLE_MOVIE = ("CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.MovieColumns.ID_MOVIE} INTEGER," +
                " ${DatabaseContract.MovieColumns.POSTER} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColumns.TITLE} TEXT NOT NULL PRIMARY KEY," +
                " ${DatabaseContract.MovieColumns.RELEASE_DATE} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColumns.VOTE_AVERAGE} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColumns.OVERVIEW} TEXT NOT NULL)")

        const val SQL_CREATE_TABLE_TV = ("CREATE TABLE $TABLEE_NAME" +
                " (${DatabaseContract.TvShowColumns.ID_TVSHOW} INTEGER," +
                " ${DatabaseContract.TvShowColumns.POSTER} TEXT NOT NULL," +
                " ${DatabaseContract.TvShowColumns.TITLE} TEXT NOT NULL PRIMARY KEY," +
                " ${DatabaseContract.TvShowColumns.RELEASE_DATE} TEXT NOT NULL," +
                " ${DatabaseContract.TvShowColumns.VOTE_AVERAGE} TEXT NOT NULL," +
                " ${DatabaseContract.TvShowColumns.OVERVIEW} TEXT NOT NULL)")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_TV)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLEE_NAME")
        onCreate(db)
    }
}