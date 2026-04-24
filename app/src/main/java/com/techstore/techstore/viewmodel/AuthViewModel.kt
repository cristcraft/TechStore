package com.techstore.techstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techstore.techstore.data.model.User
import com.techstore.techstore.data.model.UserRole
import com.techstore.techstore.data.repository.AuthRepository
import com.techstore.techstore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _authState = MutableLiveData<Resource<User>>()
    val authState: LiveData<Resource<User>> = _authState

    val isLoggedIn get() = authRepo.currentUser != null
    val currentUserId get() = authRepo.currentUser?.uid

    fun login(email: String, password: String) {
        _authState.value = Resource.Loading()
        viewModelScope.launch {
            _authState.value = authRepo.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String, role: UserRole) {
        _authState.value = Resource.Loading()
        viewModelScope.launch {
            _authState.value = authRepo.register(name, email, password, role)
        }
    }

    fun loginWithGoogle(idToken: String) {
        _authState.value = Resource.Loading()
        viewModelScope.launch {
            _authState.value = authRepo.loginWithGoogle(idToken)
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch { authRepo.resetPassword(email) }
    }

    fun logout() { authRepo.logout() }
}

