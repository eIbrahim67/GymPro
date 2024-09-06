package com.eibrahim67.gympro.myTrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.myTrainer.viewModel.MyTrainerViewModel

class MyTrainerFragment : Fragment() {

    companion object {
        fun newInstance() = MyTrainerFragment()
    }

    private val viewModel: MyTrainerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_trainer, container, false)
    }
}