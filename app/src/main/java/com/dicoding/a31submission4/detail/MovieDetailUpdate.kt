package com.dicoding.a31submission4.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.db.MovieFavoriteHelper
import com.dicoding.a31submission4.helper.MappingHelper
import com.dicoding.a31submission4.model.MovieFavoriteModel
import com.dicoding.a31submission4.model.MovieModel
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailUpdate : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val REQUEST_ADD = 100
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        const val RESULT_DELETE = 301
        var position: Int = 0
    }
    private var favorite: MovieFavoriteModel? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var movieeHelper: MovieFavoriteHelper
    private var movieHelper: MovieFavoriteHelper? = null
    private var movie = arrayListOf<MovieModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_update)

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Favorite"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = movieHelper?.deleteMovie(favorite?.id.toString())!!.toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@MovieDetailUpdate, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}