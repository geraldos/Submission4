package com.dicoding.a31submission4.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.detail.MovieDetailUpdate
import com.dicoding.a31submission4.fragment.MovieFavoriteFragment
import com.dicoding.a31submission4.model.MovieFavoriteModel
import com.dicoding.a31submission4.model.MovieModel
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieFavoriteAdapter(movieFavoriteFragment: MovieFavoriteFragment) : RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {
    var listMovieFavorite = ArrayList<MovieFavoriteModel>()
        set(listMovieFavorite) {
            if (listMovieFavorite.size > 0) {
                this.listMovieFavorite.clear()
            }
            this.listMovieFavorite.addAll(listMovieFavorite)
            notifyDataSetChanged()
        }

    fun addItem(favoritemovie: MovieFavoriteModel) {
        this.listMovieFavorite.add(favoritemovie)
        notifyItemInserted(this.listMovieFavorite.size - 1)
    }

    fun updateItem(position: Int, favoritemovie: MovieFavoriteModel) {
        this.listMovieFavorite[position] = MovieFavoriteModel()
        notifyItemChanged(position, favoritemovie)
    }

    fun removeItem(position: Int) {
        this.listMovieFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovieFavorite.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieFavoriteViewHolder, position: Int) {
        holder.bind(listMovieFavorite[position])
    }

    override fun getItemCount(): Int = this.listMovieFavorite.size

    inner class MovieFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(moviefavorite: MovieFavoriteModel) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w342/${moviefavorite.poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(img_item_photo)
                tv_item_name.text = moviefavorite.title
                tv_item_release.text = moviefavorite.release_date
                tv_item_rating.text = moviefavorite.vote_average
                tv_item_description.text = moviefavorite.overview

                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "Kamu memilih ${moviefavorite.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(itemView.context, MovieDetailUpdate::class.java)
                    val movies = ArrayList<MovieModel>()
                    val loMovie = MovieModel(
                        title = moviefavorite.title,
                        overview = moviefavorite.overview,
                        vote_average = moviefavorite.vote_average,
                        release_date = moviefavorite.release_date,
                        poster = moviefavorite.poster
                    )
                    movies.add(loMovie)
                    intent.putExtra(MovieDetailUpdate.EXTRA_MOVIE,movies)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}