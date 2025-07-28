package com.eibrahim67.gympro.securityPolicies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentSecurityPoliciesBinding

class SecurityPoliciesFragment : Fragment() {

    private var _binding: FragmentSecurityPoliciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SecurityPoliciesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityPoliciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe back button pop
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Use viewModel if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
