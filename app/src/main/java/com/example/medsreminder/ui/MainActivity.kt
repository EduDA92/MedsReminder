package com.example.medsreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        
        /* DestinationChangedListener to show the bottomNavBar only in the top level destination fragments */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dashboard -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
                R.id.calendar -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
                R.id.appointments -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
                R.id.user -> {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomBar.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                }
            }
        }

        binding.bottomNav.setupWithNavController(navController)

    }


}