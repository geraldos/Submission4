package com.dicoding.a31submission4.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieFavoriteModel(
    var id: Int = 0,
    var poster: String? = null,
    var title: String? = null,
    var release_date: String? = null,
    var vote_average: String? = null,
    var overview: String? = null

) : Parcelable