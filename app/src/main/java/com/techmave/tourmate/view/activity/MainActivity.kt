package com.techmave.tourmate.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.techmave.tourmate.R
import com.techmave.tourmate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var configuration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

        supportFragmentManager.findFragmentById(R.id.fragment_container)?.findNavController()?.let {

            configuration = AppBarConfiguration.Builder(R.id.fragment_login, R.id.fragment_register).build()

            NavigationUI.setupWithNavController(binding.toolbar, it, configuration)
            binding.bottomNavigation.setupWithNavController(it)
        }
    }
}