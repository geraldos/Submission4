package com.dicoding.a31submission4.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.adapter.MovieFavoriteAdapter
import com.dicoding.a31submission4.db.MovieFavoriteHelper
import com.dicoding.a31submission4.detail.MovieDetailUpdate
import com.dicoding.a31submission4.helper.MappingHelper
import com.dicoding.a31submission4.model.MovieFavoriteModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_favorite.*
import kotlinx.android.synthetic.main.activity_movie_favorite.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavoriteFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: MovieFavoriteAdapter
    private lateinit var movieHelper: MovieFavoriteHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewMovie = inflater.inflate(R.layout.activity_movie_favorite, container, false)
        return viewMovie
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite.layoutManager = LinearLayoutManager(context)
        rv_favorite.setHasFixedSize(true)
        adapter = MovieFavoriteAdapter(this)
        rv_favorite.adapter = adapter

        movieHelper = MovieFavoriteHelper.getInstance(context)
        movieHelper.open()

        loadNotesAsync()

        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<MovieFavoriteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listMovieFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovieFavorite)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor =movieHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val movies = deferredNotes.await()
            if (movies.size > 0) {
                adapter.listMovieFavorite = movies
            } else {
                adapter.listMovieFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                MovieDetailUpdate.REQUEST_ADD -> if (resultCode == MovieDetailUpdate.RESULT_ADD) {
                    val movie = data.getParcelableExtra<MovieFavoriteModel>(MovieDetailUpdate.EXTRA_MOVIE)
                    adapter.addItem(movie!!)
                    rv_favorite.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                MovieDetailUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        MovieDetailUpdate.RESULT_UPDATE -> {
                            val movie = data.getParcelableExtra<MovieFavoriteModel>(MovieDetailUpdate.EXTRA_MOVIE)
                            val position = data.getIntExtra(MovieDetailUpdate.EXTRA_POSITION, 0)
                            adapter.updateItem(position, movie!!)
                            rv_favorite.smoothScrollToPosition(position)
                            showSnackbarMessage("Satu item berhasil diubah")
                        }
                        MovieDetailUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(MovieDetailUpdate.EXTRA_POSITION, 0)
                            adapter.removeItem(position)
                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }
}