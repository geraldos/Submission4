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
import com.dicoding.a31submission4.detail.TvShowDetail
import com.dicoding.a31submission4.model.TvShowModel
import kotlinx.android.synthetic.main.item_tvshow.view.*

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.CardViewViewHolder>() {

    private val mData = ArrayList<TvShowModel>()
    fun setData(items: ArrayList<TvShowModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        position: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tvshow, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShowsI: TvShowModel) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w342/${tvShowsI.poster}")
                    .apply(RequestOptions().override(350, 550))
                    .into(img_item_photo)
                tv_item_name.text = tvShowsI.title
                tv_item_release.text = tvShowsI.release_date
                tv_item_rating.text = tvShowsI.vote_average
                tv_item_description.text = tvShowsI.overview

                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "Kamu memilih ${tvShowsI.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(itemView.context, TvShowDetail::class.java)
                    val tvshows = ArrayList<TvShowModel>()
                    val loTvShow = TvShowModel(
                        title = tvShowsI.title,
                        overview = tvShowsI.overview,
                        vote_average = tvShowsI.vote_average,
                        release_date = tvShowsI.release_date,
                        poster = tvShowsI.poster
                    )
                    tvshows.add(loTvShow)
                    intent.putExtra(TvShowDetail.EXTRA_TV,tvshows)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}