package com.dicoding.a31submission4.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MovieColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "tb_movie_favorite"
            const val ID_MOVIE = "id_movie"
            const val POSTER = "poster"
            const val TITLE = "title"
            const val RELEASE_DATE = "release_date"
            const val VOTE_AVERAGE = "vote_average"
            const val OVERVIEW = "overview"
        }
    }

    internal class TvShowColumns : BaseColumns {
        companion object {
            const val TABLEE_NAME = "tb_tvShow_favorite"
            const val ID_TVSHOW = "id_tvShow"
            const val POSTER = "poster"
            const val TITLE = "title"
            const val RELEASE_DATE = "release_date"
            const val VOTE_AVERAGE = "vote_average"
            const val OVERVIEW = "overview"
        }
    }
}