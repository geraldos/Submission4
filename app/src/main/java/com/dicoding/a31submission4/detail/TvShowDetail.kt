package com.dicoding.a31submission4.detail

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.db.DatabaseContract
import com.dicoding.a31submission4.db.TvShowFavoriteHelper
import com.dicoding.a31submission4.model.TvShowFavoriteModel
import com.dicoding.a31submission4.model.TvShowModel
import kotlinx.android.synthetic.main.activity_movie_detail.btn_add_favorite
import kotlinx.android.synthetic.main.activity_movie_detail.tv_description
import kotlinx.android.synthetic.main.activity_movie_detail.tv_nama_judul
import kotlinx.android.synthetic.main.activity_movie_detail.tv_rating
import kotlinx.android.synthetic.main.activity_movie_detail.tv_release

class TvShowDetail: AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_TV = "extra_tv"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        var position: Int = 0
    }
    private var favorite: TvShowFavoriteModel? = null
    private var isFavorite = false
    private var tvHelper: TvShowFavoriteHelper? = null
    private var tvshow = arrayListOf<TvShowModel>()
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)
        progressBar = findViewById(R.id.progressBar)
        showLoading(true)

        tvHelper = TvShowFavoriteHelper.getInstance(context = applicationContext)
        tvshow = intent.getParcelableArrayListExtra<TvShowModel>(EXTRA_TV)

        progressBar.visibility = View.VISIBLE
        val handler = Handler()
        Thread(Runnable {
            try {
                Thread.sleep(4000)
            } catch (e: Exception) {

            }
            handler.post {
                tv_nama_judul.text = tvshow[0].title
                tv_description.text = tvshow[0].overview
                tv_rating.text = tvshow[0].vote_average
                tv_release.text = tvshow[0].release_date
                val imgPoster: ImageView = findViewById(R.id.tv_image)
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w780/${tvshow[0].poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(imgPoster)
                showLoading(false)
            }
        }).start()
        btn_add_favorite.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        val title = tvshow[0].title
        val vote_average = tvshow[0].vote_average
        val release_date = tvshow[0].release_date
        val overview = tvshow[0].overview
        val poster = tvshow[0].poster


        favorite = TvShowFavoriteModel(
            poster = poster,
            title = title,
            release_date = release_date,
            vote_average = vote_average,
            overview = overview
        )

        Log.d("debug", favorite?.title)

        val intent = Intent()
        intent.putExtra(EXTRA_TV, favorite)
        intent.putExtra(EXTRA_POSITION, favorite)

        val values = ContentValues()
        values.put(DatabaseContract.MovieColumns.POSTER, poster)
        values.put(DatabaseContract.MovieColumns.TITLE, title)
        values.put(DatabaseContract.MovieColumns.RELEASE_DATE, release_date)
        values.put(DatabaseContract.MovieColumns.VOTE_AVERAGE, vote_average)
        values.put(DatabaseContract.MovieColumns.OVERVIEW, overview)

        if (!isFavorite) {
            val result = tvHelper!!.insertTv(favorite)
            if (result > 0) {
                setResult(RESULT_ADD, intent)
                Toast.makeText(
                    this@TvShowDetail,
                    "Data Favorit Movie Berhasil Ditambahakan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        } else {
            Toast.makeText(
                this@TvShowDetail,
                "Data Favorite Movie Gagal Ditambahkan",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}