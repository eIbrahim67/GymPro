package com.eibrahim67.gympro.becomeTrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentBecomeTrainerBinding

class BecomeTrainerFragment : Fragment() {

    private var _binding: FragmentBecomeTrainerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BecomeTrainerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBecomeTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back button safely
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Observe and bind ViewModel data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
