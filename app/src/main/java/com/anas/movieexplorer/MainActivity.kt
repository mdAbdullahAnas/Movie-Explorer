package com.anas.movieexplorer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.anas.movieexplorer.ui.favourite.FavoritesFragment
import com.anas.movieexplorer.ui.home.HomeFragment
import com.anas.movieexplorer.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContentView(R.layout.activity_main)

        setupBottomNavigation()

        // Default screen
        if (savedInstanceState == null) {
            openFragment(HomeFragment())
            selectBottomItem("home")
        }
    }

    private fun setupBottomNavigation() {

        // HOME
        findViewById<View>(R.id.navHome).setOnClickListener {
            openFragment(HomeFragment())
            selectBottomItem("home")
        }

        // SEARCH
        findViewById<View>(R.id.navSearch).setOnClickListener {
            openSearchScreen()
        }

        // FAVORITES
        findViewById<View>(R.id.navFavorites).setOnClickListener {
            openFragment(FavoritesFragment())
            selectBottomItem("favorites")
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainFragmentContainer,
                fragment
            )
            .commit()
    }

    // Open Search Screen
    fun openSearchScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainFragmentContainer,
                SearchFragment()
            )
            .addToBackStack(null)
            .commit()

        selectBottomItem("search")
    }

    private fun selectBottomItem(selected: String) {

        val iconHome =
            findViewById<ImageView>(R.id.iconHome)

        val iconSearch =
            findViewById<ImageView>(R.id.iconSearch)

        val iconFavorites =
            findViewById<ImageView>(R.id.iconFavorites)

        val textHome =
            findViewById<TextView>(R.id.textHome)

        val textSearch =
            findViewById<TextView>(R.id.textSearch)

        val textFavorites =
            findViewById<TextView>(R.id.textFavorites)

        val activeColor =
            Color.rgb(25, 118, 210)

        val inactiveColor =
            Color.rgb(117, 117, 117)

        // Reset all
        iconHome.setColorFilter(inactiveColor)
        iconSearch.setColorFilter(inactiveColor)
        iconFavorites.setColorFilter(inactiveColor)

        textHome.setTextColor(inactiveColor)
        textSearch.setTextColor(inactiveColor)
        textFavorites.setTextColor(inactiveColor)

        // Active item
        when (selected) {

            "home" -> {
                iconHome.setColorFilter(activeColor)
                textHome.setTextColor(activeColor)
            }

            "search" -> {
                iconSearch.setColorFilter(activeColor)
                textSearch.setTextColor(activeColor)
            }

            "favorites" -> {
                iconFavorites.setColorFilter(activeColor)
                textFavorites.setTextColor(activeColor)
            }
        }
    }
}