package com.eibrahim67.gympro.mainActivity.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.mainActivity.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var navController: NavController? = null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.navigateToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                bottomNavigationView.selectedItemId = fragmentId
            }
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        navController =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)?.findNavController()

        navController?.let {
            bottomNavigationView.setupWithNavController(it)
        }

    }
}