package com.techtitans.veeraconnect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.techtitans.veeraconnect.R
import com.techtitans.veeraconnect.data.FirebaseAuthRepository
import com.techtitans.veeraconnect.databinding.FragmentRegisterBinding
import com.techtitans.veeraconnect.util.Resource
import com.techtitans.veeraconnect.util.Validators
import com.techtitans.veeraconnect.viewmodel.AuthViewModel
import com.techtitans.veeraconnect.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.btnGoLogin.setOnClickListener { findNavController().popBackStack() }

        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            val loading = state is Resource.Loading
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !loading
            when (state) {
                is Resource.Success -> {
                    viewModel.consumeRegister()
                    Snackbar.make(binding.root, R.string.success_registered, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_register_to_home)
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.consumeRegister()
                }
                else -> Unit
            }
        }
    }

    private fun attemptRegister() {
        val name = Validators.sanitize(binding.etName.text.toString())
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirm.text.toString()

        binding.tilName.error = if (Validators.isValidName(name)) null else getString(R.string.error_name)
        binding.tilEmail.error = if (Validators.isValidEmail(email)) null else getString(R.string.error_email)
        binding.tilPassword.error =
            if (Validators.isStrongPassword(password)) null else getString(R.string.error_password)
        binding.tilConfirm.error =
            if (password == confirm) null else getString(R.string.error_password_match)

        val valid = listOf(binding.tilName, binding.tilEmail, binding.tilPassword, binding.tilConfirm)
            .all { it.error == null }
        if (valid) viewModel.register(name, email, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
