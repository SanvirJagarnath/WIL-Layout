package com.techtitans.veeraconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.techtitans.veeraconnect.data.AuthRepository
import com.techtitans.veeraconnect.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Boolean>?>()
    val loginState: LiveData<Resource<Boolean>?> = _loginState

    private val _registerState = MutableLiveData<Resource<Unit>?>()
    val registerState: LiveData<Resource<Unit>?> = _registerState

    fun isLoggedIn() = repo.isLoggedIn()
    fun currentEmail() = repo.currentEmail()

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading
        viewModelScope.launch { _loginState.value = repo.login(email.trim(), password) }
    }

    fun register(name: String, email: String, password: String) {
        _registerState.value = Resource.Loading
        viewModelScope.launch { _registerState.value = repo.register(name, email.trim(), password) }
    }

    fun logout() = repo.logout()

    /** Clear one-shot results so they are not replayed after rotation or back navigation. */
    fun consumeLogin() { _loginState.value = null }
    fun consumeRegister() { _registerState.value = null }
}

/** Factory pattern (from the Task 1 design patterns section) injects the repository. */
class AuthViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(repo) as T
}
