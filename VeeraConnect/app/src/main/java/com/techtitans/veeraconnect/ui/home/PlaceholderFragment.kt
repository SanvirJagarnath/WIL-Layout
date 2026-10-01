package com.techtitans.veeraconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.techtitans.veeraconnect.databinding.FragmentPlaceholderBinding

/** Temporary screen for modules that are not built yet. Title comes from the nav graph label. */
class PlaceholderFragment : Fragment() {

    private var _binding: FragmentPlaceholderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentPlaceholderBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvScreenTitle.text = findNavController().currentDestination?.label
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
