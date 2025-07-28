package com.eibrahim67.gympro.myProgress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentMyProgressBinding

class MyProgressFragment : Fragment() {

    private var _binding: FragmentMyProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyProgressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Optional back button handler
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Use viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
