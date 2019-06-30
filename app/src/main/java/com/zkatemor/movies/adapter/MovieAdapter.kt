package com.zkatemor.movies.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zkatemor.movies.R
import com.zkatemor.movies.essence.Movie
import com.zkatemor.movies.util.getPreferences
import com.zkatemor.movies.util.isLiked

/**
 * адаптер для просмотра списка фильмов
 */
class MovieAdapter(private val items: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null
    var onLikeClick: ((ImageView, Movie) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewHolder: ViewGroup, position: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(viewHolder.context).inflate(
                R.layout.movie_card,
                viewHolder, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        val item = items[position]

        viewHolder.name.text = item.getName
        viewHolder.description.text = item.getDescription

        if (item.getDate != "")
            viewHolder.date.text = item.getDate
        else
            viewHolder.date.text = "дата неизвестна"

        val iconLiked =
            if (viewHolder.main_layout.context.getPreferences().isLiked(item.getId))
                R.drawable.ic_heart_fill
            else
                R.drawable.ic_heart

        item.isFavorites = iconLiked == R.drawable.ic_heart_fill

        viewHolder.heart.setImageResource(iconLiked)

        Glide.with(viewHolder.main_layout).load(item.getImageURL).into(viewHolder.image)
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var main_layout: LinearLayout
        var name: TextView
        var description: TextView
        var date: TextView
        var image: ImageView
        var heart: ImageView

        init {
            super.itemView
            main_layout = itemView.findViewById(R.id.main_layout) as LinearLayout
            name = itemView.findViewById(R.id.movie_name) as TextView
            description = itemView.findViewById(R.id.movie_description) as TextView
            date = itemView.findViewById(R.id.text_date) as TextView
            image = itemView.findViewById(R.id.image_view_movie) as ImageView
            heart = itemView.findViewById(R.id.heart) as ImageView

            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }

            heart.setOnClickListener{
                onLikeClick!!(heart, items[adapterPosition])
            }

        }
    }
}