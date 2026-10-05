package com.anas.movieexplorer.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.anas.movieexplorer.R
import com.anas.movieexplorer.data.remote.MovieDto
import com.anas.movieexplorer.databinding.ItemSearchMovieBinding

class SearchAdapter(
    private val onMovieClick: (MovieDto) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val movies = mutableListOf<MovieDto>()

    fun submitList(newMovies: List<MovieDto>) {

        movies.clear()
        movies.addAll(newMovies)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {

        val binding =
            ItemSearchMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int =
        movies.size

    inner class SearchViewHolder(
        private val binding: ItemSearchMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto) {

            binding.tvTitle.text = movie.title

            val year =
                movie.release_date
                    ?.takeIf { it.length >= 4 }
                    ?.substring(0, 4)
                    ?: ""

            binding.tvYear.text = year

            binding.tvRating.text =
                String.format(
                    "★ %.1f",
                    movie.vote_average
                )

            val posterUrl =
                movie.poster_path?.let {
                    "https://image.tmdb.org/t/p/w185$it"
                }

            binding.imgPoster.load(posterUrl) {

                crossfade(true)

                placeholder(R.drawable.ic_movie_placeholder)

                error(R.drawable.ic_movie_placeholder)
            }

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}