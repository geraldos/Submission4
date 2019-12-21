package com.dicoding.a31submission4.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.adapter.TvShowFavoriteAdapter
import com.dicoding.a31submission4.db.TvShowFavoriteHelper
import com.dicoding.a31submission4.detail.TvShowDetailUpdate
import com.dicoding.a31submission4.helper.MappingHelper
import com.dicoding.a31submission4.model.TvShowFavoriteModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_tvshow_favorite.*
import kotlinx.android.synthetic.main.fragment_tvshow_favorite.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvShowFavoriteFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: TvShowFavoriteAdapter
    private lateinit var tvHelper: TvShowFavoriteHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewTvShow = inflater.inflate(R.layout.fragment_tvshow_favorite, container, false)
        return viewTvShow
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite.layoutManager = LinearLayoutManager(context)
        rv_favorite.setHasFixedSize(true)
        adapter = TvShowFavoriteAdapter(this)
        rv_favorite.adapter = adapter

        tvHelper = TvShowFavoriteHelper.getInstance(context)
        tvHelper.open()

        loadNotesAsync()

        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShowFavoriteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listTvFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listTvFavorite)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor =tvHelper.queryAll()
                MappingHelper.mapCursorToArrayListt(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val tvShow = deferredNotes.await()
            if (tvShow.size > 0) {
                adapter.listTvFavorite = tvShow
            } else {
                adapter.listTvFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                TvShowDetailUpdate.REQUEST_ADD -> if (resultCode == TvShowDetailUpdate.RESULT_ADD) {
                    val tvShow = data.getParcelableExtra<TvShowFavoriteModel>(TvShowDetailUpdate.EXTRA_TV)
                    adapter.addItem(tvShow!!)
                    rv_favorite.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                TvShowDetailUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        TvShowDetailUpdate.RESULT_UPDATE -> {
                            val tvShow = data.getParcelableExtra<TvShowFavoriteModel>(TvShowDetailUpdate.EXTRA_TV)
                            val position = data.getIntExtra(TvShowDetailUpdate.EXTRA_POSITION, 0)
                            adapter.updateItem(position, tvShow!!)
                            rv_favorite.smoothScrollToPosition(position)
                            showSnackbarMessage("Satu item berhasil diubah")
                        }
                        TvShowDetailUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(TvShowDetailUpdate.EXTRA_POSITION, 0)
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
        tvHelper.close()
    }
}