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
import com.dicoding.a31submission4.db.MovieFavoriteHelper
import com.dicoding.a31submission4.helper.MappingHelper
import com.dicoding.a31submission4.model.MovieFavoriteModel
import com.dicoding.a31submission4.model.MovieModel
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetail : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        var position: Int = 0
    }
    private var favorite: MovieModel? = null
    private lateinit var progressBar: ProgressBar
    private var isFavorite = false
    private var movieHelper: MovieFavoriteHelper? = null
    private var movie = arrayListOf<MovieModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        progressBar = findViewById(R.id.progressBar)
        showLoading(true)

        movieHelper = MovieFavoriteHelper.getInstance(context = applicationContext)
        movie = intent.getParcelableArrayListExtra<MovieModel>(EXTRA_MOVIE)

        val handler = Handler()
        Thread(Runnable {
            try {
                Thread.sleep(4000)
            } catch (e: Exception) {
            }
            handler.post {
                tv_nama_judul.text = movie[0].title
                tv_description.text = movie[0].overview
                tv_rating.text = movie[0].vote_average
                tv_release.text = movie[0].release_date
                val imgPoster: ImageView = findViewById(R.id.tv_image)
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w780/${movie[0].poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(imgPoster)
                showLoading(false)
            }
        }).start()
        btn_add_favorite.setOnClickListener(this)
    }


    override fun onClick(view: View) {

        val title = movie[0].title
        val vote_average = movie[0].vote_average
        val release_date = movie[0].release_date
        val overview = movie[0].overview
        val poster = movie[0].poster


        favorite = MovieModel(
            poster = poster,
            title = title,
            release_date = release_date,
            vote_average = vote_average,
            overview = overview
        )
        Log.d("debug", favorite?.poster)


        val intent = Intent()
        intent.putExtra(EXTRA_MOVIE, favorite)
        intent.putExtra(EXTRA_POSITION, favorite)

        val values = ContentValues()
        values.put(DatabaseContract.MovieColumns.POSTER, poster)
        values.put(DatabaseContract.MovieColumns.TITLE, title)
        values.put(DatabaseContract.MovieColumns.RELEASE_DATE, release_date)
        values.put(DatabaseContract.MovieColumns.VOTE_AVERAGE, vote_average)
        values.put(DatabaseContract.MovieColumns.OVERVIEW, overview)

        if (!isFavorite) {
        val result = movieHelper!!.insertMovie(favorite)
        if (result >= 0) {
            setResult(RESULT_ADD, intent)
            Toast.makeText(
                this@MovieDetail,
                "Data Favorit Movie Berhasil Ditambahakan",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        } else {
            Toast.makeText(
                this@MovieDetail,
                "Data Favorite Movie Sudah Ditambahkan",
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