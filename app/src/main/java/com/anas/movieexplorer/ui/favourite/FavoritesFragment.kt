package com.anas.movieexplorer.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anas.movieexplorer.data.local.MovieDatabase
import com.anas.movieexplorer.data.repository.FavoriteRepository
import com.anas.movieexplorer.databinding.FragmentFavoritesBinding
import com.anas.movieexplorer.ui.details.DetailsFragment
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteAdapter

    private val favoriteViewModel: FavoriteViewModel by lazy {

        val database =
            MovieDatabase.getDatabase(requireContext())

        val repository =
            FavoriteRepository(
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

        _binding = FragmentFavoritesBinding.inflate(
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
        observeFavorites()
    }

    private fun setupRecyclerView() {

        adapter = FavoriteAdapter(

            onMovieClick = { movie ->

                parentFragmentManager.beginTransaction()
                    .replace(
                        com.anas.movieexplorer.R.id.mainFragmentContainer,
                        DetailsFragment.newInstance(movie.id)
                    )
                    .addToBackStack(null)
                    .commit()
            },

            onRemoveClick = { movie ->

                favoriteViewModel.removeFavorite(
                    movie.id
                )
            }
        )

        binding.recyclerFavorites.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter =
                this@FavoritesFragment.adapter

            setHasFixedSize(true)
        }
    }

    private fun observeFavorites() {

        viewLifecycleOwner.lifecycleScope.launch {

            favoriteViewModel
                .getFavorites()
                .collect { movies ->

                    adapter.submitList(movies)

                    if (movies.isEmpty()) {

                        binding.recyclerFavorites.visibility =
                            View.GONE

                        binding.emptyState.visibility =
                            View.VISIBLE

                    } else {

                        binding.recyclerFavorites.visibility =
                            View.VISIBLE

                        binding.emptyState.visibility =
                            View.GONE
                    }
                }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}