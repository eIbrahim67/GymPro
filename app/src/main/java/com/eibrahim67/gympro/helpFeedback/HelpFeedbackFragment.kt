package com.eibrahim67.gympro.helpFeedback

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.databinding.FragmentHelpFeedbackBinding
import kotlinx.coroutines.launch

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

        observeViewModel()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun observeViewModel() {
        viewModel.loadHelpAndFeedbackContent()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.helpAndFeedbackContent.collect { content ->
                    try {
                        binding.text.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
                    } catch (e: Exception) {
                        Log.e("HelpFeedback", "Error updating description text: ${e.message}")
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
