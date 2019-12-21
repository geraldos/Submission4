package com.dicoding.a31submission4.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.adapter.MovieAdapter
import com.dicoding.a31submission4.viewmodel.MovieMainModelView

class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var mainViewModel: MovieMainModelView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewMovie = inflater.inflate(R.layout.fragment_movie, container, false)
        return viewMovie
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = view.findViewById<View>(R.id.rv_movie) as RecyclerView

        adapter = MovieAdapter()
        movie.layoutManager = LinearLayoutManager(context)
        movie.adapter = adapter
        progressBar = view.findViewById(R.id.progressBar)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieMainModelView::class.java)
        mainViewModel.setMovie()
        mainViewModel.getmovies().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null) {
                adapter.setData(movieItems)
                showLoading(false)
            }
        })
        mainViewModel.setMovie()
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}