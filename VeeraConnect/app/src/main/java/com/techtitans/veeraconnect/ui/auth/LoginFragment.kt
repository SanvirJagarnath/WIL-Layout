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
import com.techtitans.veeraconnect.databinding.FragmentLoginBinding
import com.techtitans.veeraconnect.util.Resource
import com.techtitans.veeraconnect.util.Validators
import com.techtitans.veeraconnect.viewmodel.AuthViewModel
import com.techtitans.veeraconnect.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Already signed in: skip straight to the app
        if (viewModel.isLoggedIn()) {
            findNavController().navigate(R.id.action_login_to_home)
            return
        }

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            setLoading(state is Resource.Loading)
            when (state) {
                is Resource.Success -> {
                    // TODO: if state.data (isAdmin) is true, navigate to the admin dashboard
                    viewModel.consumeLogin()
                    findNavController().navigate(R.id.action_login_to_home)
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.consumeLogin()
                }
                else -> Unit
            }
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        binding.tilEmail.error =
            if (Validators.isValidEmail(email)) null else getString(R.string.error_email)
        binding.tilPassword.error =
            if (password.isNotEmpty()) null else getString(R.string.error_password)

        if (binding.tilEmail.error == null && binding.tilPassword.error == null) {
            viewModel.login(email, password)
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.btnGoRegister.isEnabled = !loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
