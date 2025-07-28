package com.eibrahim67.gympro.myTrainingPlans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentMyTrainingPlansBinding

class MyTrainingPlansFragment : Fragment() {

    private var _binding: FragmentMyTrainingPlansBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyTrainingPlansViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTrainingPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe back button handler
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Use the viewModel if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
