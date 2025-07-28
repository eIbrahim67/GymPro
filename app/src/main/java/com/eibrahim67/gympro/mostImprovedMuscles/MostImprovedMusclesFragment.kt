package com.eibrahim67.gympro.mostImprovedMuscles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentMostImprovedMusclesBinding

class MostImprovedMusclesFragment : Fragment() {

    private var _binding: FragmentMostImprovedMusclesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MostImprovedMusclesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMostImprovedMusclesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Optional back button handler
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Use viewModel as needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
