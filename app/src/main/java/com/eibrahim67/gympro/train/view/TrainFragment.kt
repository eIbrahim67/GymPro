package com.eibrahim67.gympro.train.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.train.viewModel.TrainViewModel

class TrainFragment : Fragment() {

    companion object {
        fun newInstance() = TrainFragment()
    }

    private val viewModel: TrainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_train, container, false)
    }
}