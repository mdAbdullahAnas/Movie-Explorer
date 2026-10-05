package com.anas.movieexplorer.ui.details

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.anas.movieexplorer.BuildConfig
import com.anas.movieexplorer.R
import com.anas.movieexplorer.data.local.FavoriteMovieEntity
import com.anas.movieexplorer.data.local.MovieDatabase
import com.anas.movieexplorer.data.remote.MovieApiService
import com.anas.movieexplorer.data.repository.FavoriteRepository
import com.anas.movieexplorer.databinding.FragmentDetailsBinding

import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

import com.anas.movieexplorer.ui.favourite.FavoriteViewModel
import com.anas.movieexplorer.ui.favourite.FavoriteViewModelFactory


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var shimmerAnimator: ObjectAnimator? = null

    private var currentMovie: FavoriteMovieEntity? = null

    private val api: MovieApiService by lazy {

        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

    private val favoriteViewModel: FavoriteViewModel by lazy {

        val database = MovieDatabase.getDatabase(requireContext())

        val repository = FavoriteRepository(
            database.favoriteMovieDao()
        )

        ViewModelProvider(
            this,
            FavoriteViewModelFactory(repository)
        )[FavoriteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val movieId =
            arguments?.getInt(ARG_MOVIE_ID, -1) ?: -1

        if (movieId == -1) {
            return
        }

        setupButtons()
        observeFavourite(movieId)
        loadMovieDetails(movieId)
    }

    // --------------------------------------------------
    // FAVOURITE STATE
    // --------------------------------------------------

    private fun observeFavourite(movieId: Int) {

        viewLifecycleOwner.lifecycleScope.launch {

            favoriteViewModel
                .isFavorite(movieId)
                .collect { isFavorite ->

                    updateFavouriteUI(isFavorite)
                }
        }
    }

    private fun updateFavouriteUI(isFavorite: Boolean) {

        if (isFavorite) {

            binding.btnFavourite.text =
                "♥  Added to Favourites"

            binding.btnFavouriteIcon.setImageResource(
                R.drawable.ic_favorite
            )

            binding.btnFavouriteIcon.contentDescription =
                "Remove from favourites"

        } else {

            binding.btnFavourite.text =
                "♡  Add to Favourites"

            binding.btnFavouriteIcon.setImageResource(
                R.drawable.ic_favorite_border
            )

            binding.btnFavouriteIcon.contentDescription =
                "Add to favourites"
        }
    }

    private fun toggleFavourite() {

        val movie = currentMovie ?: return

        favoriteViewModel.toggleFavorite(movie)
    }

    // --------------------------------------------------
    // BUTTONS
    // --------------------------------------------------

    private fun setupButtons() {

        binding.btnBack.setOnClickListener {

            parentFragmentManager.popBackStack()
        }

        binding.btnFavourite.setOnClickListener {

            toggleFavourite()
        }

        binding.btnFavouriteIcon.setOnClickListener {

            toggleFavourite()
        }
    }

    // --------------------------------------------------
    // LOAD DETAILS
    // --------------------------------------------------

    private fun loadMovieDetails(movieId: Int) {

        startShimmer()

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val movie =
                    api.getMovieDetails(movieId)

                // ------------------------------------------
                // TITLE
                // ------------------------------------------

                binding.movieTitle.text =
                    movie.title

                // ------------------------------------------
                // YEAR
                // ------------------------------------------

                val year =
                    movie.release_date
                        ?.takeIf { it.length >= 4 }
                        ?.substring(0, 4)
                        ?: "N/A"

                binding.releaseYear.text =
                    year

                // ------------------------------------------
                // RUNTIME
                // ------------------------------------------

                val runtime =
                    movie.runtime ?: 0

                binding.runtime.text =
                    formatRuntime(runtime)

                // ------------------------------------------
                // RATING
                // ------------------------------------------

                binding.rating.text =
                    String.format(
                        "%.1f",
                        movie.vote_average ?: 0.0
                    )

                // ------------------------------------------
                // OVERVIEW
                // ------------------------------------------

                binding.overview.text =
                    if (!movie.overview.isNullOrBlank()) {
                        movie.overview
                    } else {
                        "No overview available."
                    }

                // ------------------------------------------
                // BACKDROP
                // ------------------------------------------

                if (!movie.backdrop_path.isNullOrBlank()) {

                    val backdropUrl =
                        "https://image.tmdb.org/t/p/w780${movie.backdrop_path}"

                    binding.backdropImage.load(backdropUrl) {
                        crossfade(true)
                    }
                }

                // ------------------------------------------
                // GENRES
                // ------------------------------------------

                setupGenres(movie.genres)

                // ------------------------------------------
                // ROOM MOVIE
                // ------------------------------------------

                currentMovie = FavoriteMovieEntity(
                    id = movie.id,
                    title = movie.title,
                    posterPath = movie.poster_path,
                    backdropPath = movie.backdrop_path,
                    overview = movie.overview,
                    releaseDate = movie.release_date,
                    rating = movie.vote_average ?: 0.0
                )

                stopShimmer()

            } catch (e: Exception) {

                e.printStackTrace()

                stopShimmer()

                binding.movieTitle.text =
                    "Failed to load movie"

                binding.overview.text =
                    "Something went wrong. Please try again."
            }
        }
    }

    // --------------------------------------------------
    // SHIMMER
    // --------------------------------------------------

    private fun startShimmer() {

        binding.loadingOverlay.visibility =
            View.VISIBLE

        binding.shimmerHighlight.post {

            val parentWidth =
                binding.loadingOverlay.width

            val highlightWidth =
                binding.shimmerHighlight.width

            shimmerAnimator?.cancel()

            shimmerAnimator =
                ObjectAnimator.ofFloat(
                    binding.shimmerHighlight,
                    View.TRANSLATION_X,
                    -highlightWidth.toFloat(),
                    parentWidth.toFloat()
                ).apply {

                    duration = 1000L

                    repeatCount =
                        ObjectAnimator.INFINITE

                    interpolator =
                        LinearInterpolator()

                    start()
                }
        }
    }

    private fun stopShimmer() {

        shimmerAnimator?.cancel()
        shimmerAnimator = null

        binding.loadingOverlay.visibility =
            View.GONE
    }

    // --------------------------------------------------
    // RUNTIME
    // --------------------------------------------------

    private fun formatRuntime(
        runtime: Int
    ): String {

        if (runtime <= 0) {
            return "N/A"
        }

        val hours =
            runtime / 60

        val minutes =
            runtime % 60

        return if (hours > 0) {

            "${hours}h ${minutes}m"

        } else {

            "${minutes}m"
        }
    }

    // --------------------------------------------------
    // GENRES
    // --------------------------------------------------

    private fun setupGenres(
        genres: List<com.anas.movieexplorer.data.remote.Genre>?
    ) {

        binding.genreContainer.removeAllViews()

        genres?.forEach { genre ->

            val textView =
                TextView(requireContext())

            textView.text =
                genre.name

            textView.textSize =
                12f

            textView.setTextColor(
                resources.getColor(
                    R.color.genre_text,
                    null
                )
            )

            textView.setPadding(
                14,
                7,
                14,
                7
            )

            textView.setBackgroundResource(
                R.drawable.bg_genre
            )

            val params =
                android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(
                0,
                0,
                8,
                0
            )

            textView.layoutParams =
                params

            binding.genreContainer.addView(
                textView
            )
        }
    }

    // --------------------------------------------------
    // DESTROY
    // --------------------------------------------------

    override fun onDestroyView() {

        shimmerAnimator?.cancel()
        shimmerAnimator = null

        super.onDestroyView()

        _binding = null
    }

    companion object {

        private const val ARG_MOVIE_ID =
            "movie_id"

        fun newInstance(
            movieId: Int
        ): DetailsFragment {

            return DetailsFragment().apply {

                arguments =
                    bundleOf(
                        ARG_MOVIE_ID to movieId
                    )
            }
        }
    }
}