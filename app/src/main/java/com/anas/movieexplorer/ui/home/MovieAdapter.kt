package com.anas.movieexplorer.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.anas.movieexplorer.data.remote.MovieDto
import com.anas.movieexplorer.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (MovieDto) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movies = mutableListOf<MovieDto>()

    fun submitList(newMovies: List<MovieDto>) {

        Log.d(
            "MOVIE_TEST",
            "Adapter submitList: ${newMovies.size}"
        )

        movies.clear()
        movies.addAll(newMovies)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        Log.d(
            "MOVIE_TEST",
            "onCreateViewHolder called"
        )

        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        Log.d(
            "MOVIE_TEST",
            "onBindViewHolder: ${movies[position].title}"
        )

        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto) {

            binding.tvMovieTitle.text = movie.title

            binding.tvReleaseDate.text =
                movie.release_date ?: "Release date unavailable"

            binding.tvRating.text =
                "⭐ %.1f".format(movie.vote_average)

            val posterUrl =
                "https://image.tmdb.org/t/p/w500${movie.poster_path}"

            binding.ivMoviePoster.load(posterUrl)

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}