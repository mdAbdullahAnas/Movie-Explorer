
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

        setupPopularMovies()

        Log.d("MOVIE_TEST", "Popular RecyclerView setup done")

        setupNowPlayingMovies()

        Log.d("MOVIE_TEST", "Now Playing RecyclerView setup done")

        observeMovies()

        Log.d("MOVIE_TEST", "Movie observers started")

        viewModel.loadPopularMovies()

        Log.d("MOVIE_TEST", "Popular movies API requested")

        viewModel.loadNowPlayingMovies()

        Log.d("MOVIE_TEST", "Now Playing movies API requested")
    }

    private fun setupPopularMovies() {

        Log.d("MOVIE_TEST", "setupPopularMovies()")

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

        Log.d("MOVIE_TEST", "Popular adapter attached")
    }

    private fun setupNowPlayingMovies() {

        Log.d("MOVIE_TEST", "setupNowPlayingMovies()")

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

        Log.d("MOVIE_TEST", "Now Playing adapter attached")
    }

    private fun observeMovies() {

        Log.d("MOVIE_TEST", "observeMovies() started")

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
    }

    override fun onDestroyView() {

        Log.d("MOVIE_TEST", "HomeFragment onDestroyView")

        super.onDestroyView()

        _binding = null
    }
}
