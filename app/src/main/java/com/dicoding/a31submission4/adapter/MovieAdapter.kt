package com.dicoding.a31submission4.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.a31submission4.R
import com.dicoding.a31submission4.detail.MovieDetail
import com.dicoding.a31submission4.model.MovieModel
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.CardViewViewHolder>() {

    private val mData = ArrayList<MovieModel>()
    fun setData(items: ArrayList<MovieModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        position: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(moviesI: MovieModel) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w342/${moviesI.poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(img_item_photo)
                tv_item_name.text = moviesI.title
                tv_item_release.text = moviesI.release_date
                tv_item_rating.text = moviesI.vote_average
                tv_item_description.text = moviesI.overview

                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "Kamu memilih ${moviesI.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(itemView.context, MovieDetail::class.java)
                    val movies = ArrayList<MovieModel>()
                    val loMovie = MovieModel(
                        title = moviesI.title,
                        overview = moviesI.overview,
                        vote_average = moviesI.vote_average,
                        release_date = moviesI.release_date,
                        poster = moviesI.poster
                    )
                    movies.add(loMovie)
                    intent.putExtra(MovieDetail.EXTRA_MOVIE,movies)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}