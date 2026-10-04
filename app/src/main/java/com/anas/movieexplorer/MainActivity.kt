package com.anas.movieexplorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.anas.movieexplorer.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep content inside system safe areas
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContentView(R.layout.activity_main)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {

        findViewById<android.view.View>(R.id.navHome).setOnClickListener {
            openFragment(HomeFragment())
            selectBottomItem("home")
        }

        findViewById<android.view.View>(R.id.navSearch).setOnClickListener {
            // Search screen পরে এখানে add করব
            selectBottomItem("search")
        }

        findViewById<android.view.View>(R.id.navFavorites).setOnClickListener {
            // Favorites screen পরে এখানে add করব
            selectBottomItem("favorites")
        }
    }

    private fun openFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.mainFragmentContainer,
                fragment
            )
            .commit()
    }

    private fun selectBottomItem(selected: String) {

        val iconHome = findViewById<android.widget.ImageView>(R.id.iconHome)
        val iconSearch = findViewById<android.widget.ImageView>(R.id.iconSearch)
        val iconFavorites =
            findViewById<android.widget.ImageView>(R.id.iconFavorites)

        val textHome = findViewById<android.widget.TextView>(R.id.textHome)
        val textSearch = findViewById<android.widget.TextView>(R.id.textSearch)
        val textFavorites =
            findViewById<android.widget.TextView>(R.id.textFavorites)

        val activeColor = android.graphics.Color.rgb(25, 118, 210)
        val inactiveColor = android.graphics.Color.rgb(117, 117, 117)

        iconHome.setColorFilter(inactiveColor)
        iconSearch.setColorFilter(inactiveColor)
        iconFavorites.setColorFilter(inactiveColor)

        textHome.setTextColor(inactiveColor)
        textSearch.setTextColor(inactiveColor)
        textFavorites.setTextColor(inactiveColor)

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