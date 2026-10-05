package com.anas.movieexplorer.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anas.movieexplorer.R
import com.anas.movieexplorer.databinding.FragmentSearchBinding
import com.anas.movieexplorer.data.remote.MovieDto
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentSearchBinding.inflate(
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

        setupRecyclerView()
        setupSearch()
        observeSearch()
    }

    private fun setupRecyclerView() {

        searchAdapter =
            SearchAdapter { movie ->

                openMovieDetails(movie)
            }

        binding.recyclerViewSearch.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = searchAdapter

            setHasFixedSize(true)
        }
    }

    private fun setupSearch() {

        binding.etSearch.doAfterTextChanged { text ->

            val query =
                text?.toString() ?: ""

            binding.btnClear.visibility =
                if (query.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            viewModel.searchMovies(query)
        }

        binding.btnClear.setOnClickListener {

            binding.etSearch.text?.clear()

            binding.etSearch.requestFocus()
        }
    }

    private fun observeSearch() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.searchResults.collect { movies ->

                searchAdapter.submitList(movies)

                updateEmptyState(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.isLoading.collect { loading ->

                binding.progressBar.visibility =
                    if (loading) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.error.collect { error ->

                if (error != null) {

                    binding.tvEmpty.text =
                        "Unable to load movies"

                    binding.tvEmpty.visibility =
                        View.VISIBLE
                }
            }
        }
    }

    private fun updateEmptyState(
        movies: List<MovieDto>
    ) {

        val query =
            binding.etSearch.text
                ?.toString()
                ?.trim()
                ?: ""

        when {

            query.isEmpty() -> {

                binding.tvEmpty.text =
                    "Search for movies"

                binding.tvEmpty.visibility =
                    View.VISIBLE
            }

            movies.isEmpty() -> {

                binding.tvEmpty.text =
                    "No movies found"

                binding.tvEmpty.visibility =
                    View.VISIBLE
            }

            else -> {

                binding.tvEmpty.visibility =
                    View.GONE
            }
        }
    }

    private fun openMovieDetails(movie: MovieDto) {

        /*
         * এখানে তোমার existing DetailsFragment-এর argument
         * যেভাবে সেট করা আছে, সেটা ব্যবহার করবে।
         *
         * নিচের অংশটি যদি তোমার DetailsFragment movieId নেয়,
         * তাহলে এটা ব্যবহার করতে পারো।
         */

        val bundle = Bundle().apply {
            putInt("movie_id", movie.id)
        }

        val detailsFragment =
            com.anas.movieexplorer.ui.details.DetailsFragment()

        detailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.mainFragmentContainer,
                detailsFragment
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}