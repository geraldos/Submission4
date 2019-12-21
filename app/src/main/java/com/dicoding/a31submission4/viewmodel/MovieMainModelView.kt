package com.dicoding.a31submission4.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.a31submission4.model.MovieModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import org.json.JSONObject

class MovieMainModelView: ViewModel() {
    val listMovies = MutableLiveData<ArrayList<MovieModel>>()

    internal fun setMovie() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<MovieModel>()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=280f5bf7ad7c1e8c56a18e7f3bb60893&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = MovieModel()
                        movieItems.id = movie.getInt("id")
                        movieItems.poster = movie.getString("poster_path")
                        movieItems.title = movie.getString("title")
                        movieItems.release_date = movie.getString("release_date")
                        movieItems.vote_average = movie.getString("vote_average")
                        movieItems.overview = movie.getString("overview")
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getmovies(): LiveData<ArrayList<MovieModel>> {
        return listMovies
    }
}