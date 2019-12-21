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
import com.dicoding.a31submission4.detail.TvShowDetailUpdate
import com.dicoding.a31submission4.fragment.TvShowFavoriteFragment
import com.dicoding.a31submission4.model.TvShowFavoriteModel
import kotlinx.android.synthetic.main.item_tvshow.view.*

class TvShowFavoriteAdapter(tvShowFavoriteFragment: TvShowFavoriteFragment) : RecyclerView.Adapter<TvShowFavoriteAdapter.TvShowFavoriteViewHolder>() {
    var listTvFavorite = ArrayList<TvShowFavoriteModel>()
        set(listTvFavorite) {
            if (listTvFavorite.size > 0) {
                this.listTvFavorite.clear()
            }
            this.listTvFavorite.addAll(listTvFavorite)
            notifyDataSetChanged()
        }

    fun addItem(favoritetv: TvShowFavoriteModel) {
        this.listTvFavorite.add(favoritetv)
        notifyItemInserted(this.listTvFavorite.size - 1)
    }

    fun updateItem(position: Int, favoritetv: TvShowFavoriteModel) {
        this.listTvFavorite[position] = TvShowFavoriteModel()
        notifyItemChanged(position, favoritetv)
    }

    fun removeItem(position: Int) {
        this.listTvFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listTvFavorite.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tvshow, parent, false)
        return TvShowFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: TvShowFavoriteViewHolder, position: Int) {
        holder.bind(listTvFavorite[position])
    }

    override fun getItemCount(): Int = this.listTvFavorite.size

    inner class TvShowFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvfavorite: TvShowFavoriteModel) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w342/${tvfavorite.poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(img_item_photo)
                tv_item_name.text = tvfavorite.title
                tv_item_release.text = tvfavorite.release_date
                tv_item_rating.text = tvfavorite.vote_average
                tv_item_description.text = tvfavorite.overview


                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "Kamu memilih ${tvfavorite.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(itemView.context, TvShowDetailUpdate::class.java)
                    val tv = ArrayList<TvShowFavoriteModel>()
                    val loTv = TvShowFavoriteModel(
                        title = tvfavorite.title,
                        overview = tvfavorite.overview,
                        vote_average = tvfavorite.vote_average,
                        release_date = tvfavorite.release_date,
                        poster = tvfavorite.poster
                    )
                    tv.add(loTv)
                    intent.putExtra(TvShowDetailUpdate.EXTRA_TV,tv)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}