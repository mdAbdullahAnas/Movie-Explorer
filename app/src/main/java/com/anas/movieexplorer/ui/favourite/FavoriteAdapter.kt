package com.anas.movieexplorer.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.anas.movieexplorer.data.local.FavoriteMovieEntity
import com.anas.movieexplorer.databinding.ItemFavouriteBinding

class FavoriteAdapter(
    private val onMovieClick: (FavoriteMovieEntity) -> Unit,
    private val onRemoveClick: (FavoriteMovieEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var movies = emptyList<FavoriteMovieEntity>()

    fun submitList(newMovies: List<FavoriteMovieEntity>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {

        val binding = ItemFavouriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class FavoriteViewHolder(
        private val binding: ItemFavouriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: FavoriteMovieEntity) {

            binding.movieTitle.text = movie.title

            val year = movie.releaseDate
                ?.takeIf { it.length >= 4 }
                ?.substring(0, 4)
                ?: "N/A"

            binding.movieYear.text = year

            val ratingText = "★ ${String.format("%.1f", movie.rating)}"

            val spannable = android.text.SpannableString(ratingText)

            spannable.setSpan(
                android.text.style.ForegroundColorSpan(
                    android.graphics.Color.parseColor("#FFD700")
                ),
                0,
                1,
                android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.movieRating.text = spannable

            if (!movie.posterPath.isNullOrBlank()) {

                val posterUrl =
                    "https://image.tmdb.org/t/p/w500${movie.posterPath}"

                binding.moviePoster.load(posterUrl) {
                    crossfade(true)
                }

            } else {
                binding.moviePoster.setImageDrawable(null)
            }

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }

            binding.btnRemove.setOnClickListener {
                onRemoveClick(movie)
            }
        }
    }
}