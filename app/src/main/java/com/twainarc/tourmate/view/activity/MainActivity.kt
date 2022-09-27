package com.twainarc.tourmate.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.twainarc.tourmate.R
import com.twainarc.tourmate.databinding.ActivityMainBinding
import com.twainarc.tourmate.listener.FragmentListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity(), FragmentListener {

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

    override fun showHideBottomNav(isVisible: Boolean) {

        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}