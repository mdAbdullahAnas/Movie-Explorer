
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
import com.anas.movieexplorer.databinding.FragmentHomeBinding
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

        Log.d("MOVIE_TEST", "FragmentHomeBinding inflated")

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MOVIE_TEST", "HomeFragment onViewCreated")

        // POPULAR
        setupPopularMovies()

        Log.d(
            "MOVIE_TEST",
            "Popular RecyclerView setup done"
        )

        // NOW PLAYING
        setupNowPlayingMovies()

        Log.d(
            "MOVIE_TEST",
            "Now Playing RecyclerView setup done"
        )

        // TOP RATED
        setupTopRatedMovies()

        Log.d(
            "MOVIE_TEST",
            "Top Rated RecyclerView setup done"
        )

        // OBSERVE ALL MOVIES
        observeMovies()

        Log.d(
            "MOVIE_TEST",
            "Movie observers started"
        )

        // API CALLS
        viewModel.loadPopularMovies()

        Log.d(
            "MOVIE_TEST",
            "Popular movies API requested"
        )

        viewModel.loadNowPlayingMovies()

        Log.d(
            "MOVIE_TEST",
            "Now Playing movies API requested"
        )

        viewModel.loadTopRatedMovies()

        Log.d(
            "MOVIE_TEST",
            "Top Rated movies API requested"
        )
    }


    // --------------------------------------------------
    // POPULAR MOVIES
    // --------------------------------------------------

    private fun setupPopularMovies() {

        Log.d(
            "MOVIE_TEST",
            "setupPopularMovies()"
        )

        movieAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Popular movie clicked: ${movie.title}"
            )

            // Details screen পরে এখানে add করব
        }

        binding.recyclerViewMovies.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = movieAdapter
        }

        Log.d(
            "MOVIE_TEST",
            "Popular adapter attached"
        )
    }


    // --------------------------------------------------
    // NOW PLAYING MOVIES
    // --------------------------------------------------

    private fun setupNowPlayingMovies() {

        Log.d(
            "MOVIE_TEST",
            "setupNowPlayingMovies()"
        )

        nowPlayingAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Now Playing movie clicked: ${movie.title}"
            )

            // Details screen পরে এখানে add করব
        }

        binding.recyclerViewNowPlaying.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = nowPlayingAdapter
        }

        Log.d(
            "MOVIE_TEST",
            "Now Playing adapter attached"
        )
    }


    // --------------------------------------------------
    // TOP RATED MOVIES
    // --------------------------------------------------

    private fun setupTopRatedMovies() {

        Log.d(
            "MOVIE_TEST",
            "setupTopRatedMovies()"
        )

        topRatedAdapter = MovieAdapter { movie ->

            Log.d(
                "MOVIE_TEST",
                "Top Rated movie clicked: ${movie.title}"
            )

            // Details screen পরে এখানে add করব
        }

        binding.recyclerViewTopRated.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = topRatedAdapter
        }

        Log.d(
            "MOVIE_TEST",
            "Top Rated adapter attached"
        )
    }


    // --------------------------------------------------
    // OBSERVE MOVIES
    // --------------------------------------------------

    private fun observeMovies() {

        Log.d(
            "MOVIE_TEST",
            "observeMovies() started"
        )


        // POPULAR
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.movies.collect { movies ->

                Log.d(
                    "MOVIE_TEST",
                    "Popular movies collected: ${movies.size}"
                )

                movieAdapter.submitList(movies)
            }
        }


        // NOW PLAYING
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.nowPlayingMovies.collect { movies ->

                Log.d(
                    "MOVIE_TEST",
                    "Now Playing movies collected: ${movies.size}"
                )

                nowPlayingAdapter.submitList(movies)
            }
        }


        // TOP RATED
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

        Log.d(
            "MOVIE_TEST",
            "HomeFragment onDestroyView"
        )

        super.onDestroyView()

        _binding = null
    }
}

