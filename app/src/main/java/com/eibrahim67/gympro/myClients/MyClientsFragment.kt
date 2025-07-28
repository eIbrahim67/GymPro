package com.eibrahim67.gympro.myClients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentMyClientsBinding

class MyClientsFragment : Fragment() {

    private var _binding: FragmentMyClientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyClientsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe backBtn handler
        binding.backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
