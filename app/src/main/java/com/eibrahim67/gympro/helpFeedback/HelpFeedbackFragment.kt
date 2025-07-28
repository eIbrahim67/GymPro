package com.eibrahim67.gympro.helpFeedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentHelpFeedbackBinding

class HelpFeedbackFragment : Fragment() {

    private var _binding: FragmentHelpFeedbackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HelpFeedbackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back button safely
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Interact with ViewModel here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
