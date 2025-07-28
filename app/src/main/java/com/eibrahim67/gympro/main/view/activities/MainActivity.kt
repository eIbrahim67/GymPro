package com.eibrahim67.gympro.main.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    private val navOptionsRight = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    private val navOptionsLeft = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_left)
        .build()


    private val viewModel: MainViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(this).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.navigateRightToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                navController?.navigate(fragmentId, null, navOptionsRight)
            }
        }

        viewModel.navigateLeftToFragment.observe(this) { fragmentId ->
            fragmentId?.let {
                navController?.navigate(fragmentId, null, navOptionsLeft)
            }
        }


        navController =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
                ?.findNavController()

    }
}