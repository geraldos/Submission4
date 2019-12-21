package com.dicoding.a31submission4.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.a31submission4.model.TvShowModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import org.json.JSONObject

class TvShowMainModelView: ViewModel() {
    val listTv = MutableLiveData<ArrayList<TvShowModel>>()

    internal fun setTv() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<TvShowModel>()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=280f5bf7ad7c1e8c56a18e7f3bb60893&language=en-US"
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
                        val tvShow = list.getJSONObject(i)
                        val tvItems = TvShowModel()
                        tvItems.id = tvShow.getInt("id")
                        tvItems.poster = tvShow.getString("poster_path")
                        tvItems.title = tvShow.getString("original_name")
                        tvItems.release_date = tvShow.getString("first_air_date")
                        tvItems.vote_average = tvShow.getString("vote_average")
                        tvItems.overview = tvShow.getString("overview")
                        listItems.add(tvItems)
                    }
                    listTv.postValue(listItems)
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

    internal fun getTv(): LiveData<ArrayList<TvShowModel>> {
        return listTv
    }
}