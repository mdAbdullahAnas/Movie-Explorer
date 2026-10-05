package com.anas.movieexplorer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anas.movieexplorer.MainActivity
import com.anas.movieexplorer.R
import com.anas.movieexplorer.databinding.FragmentHomeBinding
import com.anas.movieexplorer.ui.details.DetailsFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var nowPlayingAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("MOVIE_TEST", "HomeFragment onCreateView")

        _binding = FragmentHomeBinding.inflate(
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

        // SEARCH BUTTON
        binding.btnSearch.setOnClickListener {
            (requireActivity() as MainActivity).openSearchScreen()
        }

        setupPopularMovies()
        setupNowPlayingMovies()
        setupTopRatedMovies()

        observeMovies()

        viewModel.loadPopularMovies()
        viewModel.loadNowPlayingMovies()
        viewModel.loadTopRatedMovies()
    }

    // --------------------------------------------------
    // OPEN DETAILS
    // --------------------------------------------------

    private fun openMovieDetails(movieId: Int) {

        Log.d(
            "MOVIE_TEST",
            "Opening movie details: $movieId"
        )

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.mainFragmentContainer,
                DetailsFragment.newInstance(movieId)
            )
            .addToBackStack(null)
            .commit()
    }

    // --------------------------------------------------
    // POPULAR MOVIES
    // --------------------------------------------------

    private fun setupPopularMovies() {

        movieAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Popular movie clicked: ${movie.title}"
            )

            openMovieDetails(movie.id)
        }

        binding.recyclerViewMovies.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = movieAdapter
        }
    }

    // --------------------------------------------------
    // NOW PLAYING MOVIES
    // --------------------------------------------------

    private fun setupNowPlayingMovies() {

        nowPlayingAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Now Playing movie clicked: ${movie.title}"
            )

            openMovieDetails(movie.id)
        }

        binding.recyclerViewNowPlaying.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = nowPlayingAdapter
        }
    }

    // --------------------------------------------------
    // TOP RATED MOVIES
    // --------------------------------------------------

    private fun setupTopRatedMovies() {

        topRatedAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Top Rated movie clicked: ${movie.title}"
            )

            openMovieDetails(movie.id)
        }

        binding.recyclerViewTopRated.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = topRatedAdapter
        }
    }

    // --------------------------------------------------
    // OBSERVE MOVIES
    // --------------------------------------------------

    private fun observeMovies() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.movies.collect { movies ->

                Log.d(
                    "MOVIE_TEST",
                    "Popular movies collected: ${movies.size}"
                )

                movieAdapter.submitList(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.nowPlayingMovies.collect { movies ->

                Log.d(
                    "MOVIE_TEST",
                    "Now Playing movies collected: ${movies.size}"
                )

                nowPlayingAdapter.submitList(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.topRatedMovies.collect { movies ->

                Log.d(
                    "MOVIE_TEST",
                    "Top Rated movies collected: ${movies.size}"
                )

                topRatedAdapter.submitList(movies)
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}