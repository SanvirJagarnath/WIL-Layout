package com.techtitans.veeraconnect.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.techtitans.veeraconnect.R
import com.techtitans.veeraconnect.data.FirebaseAuthRepository
import com.techtitans.veeraconnect.databinding.FragmentProfileBinding
import com.techtitans.veeraconnect.util.ThemeManager
import com.techtitans.veeraconnect.viewmodel.AuthViewModel
import com.techtitans.veeraconnect.viewmodel.AuthViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvEmail.text = viewModel.currentEmail()

        binding.switchDark.isChecked = ThemeManager.isDark(requireContext())
        binding.switchDark.setOnCheckedChangeListener { _, checked ->
            ThemeManager.setDark(requireContext(), checked)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_profile_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
