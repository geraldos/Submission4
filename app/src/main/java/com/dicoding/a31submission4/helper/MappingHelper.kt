package com.dicoding.a31submission4.helper

import android.database.Cursor
import com.dicoding.a31submission4.db.DatabaseContract
import com.dicoding.a31submission4.model.MovieFavoriteModel
import com.dicoding.a31submission4.model.TvShowFavoriteModel

object MappingHelper {

    fun mapCursorToArrayList(moviefavoritesCursor: Cursor): ArrayList<MovieFavoriteModel> {
        val movieList = ArrayList<MovieFavoriteModel>()
        moviefavoritesCursor.moveToFirst()
        while (moviefavoritesCursor.moveToNext()) {
            val _id = moviefavoritesCursor.getInt(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID_MOVIE))
            val poster =moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER))
            val title = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE))
            val release_date = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE))
            val vote_average = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTE_AVERAGE))
            val overview = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW))
            movieList.add(MovieFavoriteModel(_id, poster, title, release_date, vote_average, overview))
        }
        return movieList
    }

    fun mapCursorToArrayListt(moviefavoritesCursor: Cursor): ArrayList<TvShowFavoriteModel> {
        val tvShowList = ArrayList<TvShowFavoriteModel>()
        moviefavoritesCursor.moveToFirst()
        while (moviefavoritesCursor.moveToNext()) {
            val _id = moviefavoritesCursor.getInt(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_TVSHOW))
            val poster =moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.POSTER))
            val title = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.TITLE))
            val release_date = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.RELEASE_DATE))
            val vote_average = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.VOTE_AVERAGE))
            val overview = moviefavoritesCursor.getString(moviefavoritesCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.OVERVIEW))
            tvShowList.add(TvShowFavoriteModel(_id, poster, title, release_date, vote_average, overview))
        }
        return tvShowList
    }
}