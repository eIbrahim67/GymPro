package com.eibrahim67.gympro.core.main.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.main.viewModel.MainViewModel
import com.eibrahim67.gympro.core.main.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    private val viewModel: MainViewModel by viewModels {
        val app = application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.navigateRightToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                navController?.navigate(fragmentId, null, viewModel.navOptionsRight)
            }
        }

        viewModel.navigateLeftToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                navController?.navigate(fragmentId, null, viewModel.navOptionsLeft)
            }
        }


        navController = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
            ?.findNavController()

    }
}