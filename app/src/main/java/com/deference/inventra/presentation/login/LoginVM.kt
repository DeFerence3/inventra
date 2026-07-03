package com.deference.inventra.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.Session
import com.deference.inventra.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _eventFlow = Channel<LoginEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onAction(action: LoginActions) {
        when (action) {
            is LoginActions.OnPasswordChanged -> updateState { it.copy(password = action.password) }
            is LoginActions.OnUsernameChanged -> updateState { it.copy(username = action.username) }
            is LoginActions.DoLogin -> login()
        }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase(state.value.username,state.value.password).collectLatest { response ->
                when(response){
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false) }
                        _eventFlow.send(LoginEvent.Error(response.message))
                    }
                    RequestState.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                    is RequestState.Success -> {
                        val data = response.data
                        Session.setUserData(data)
                        updateState { it.copy(isLoading = false) }
                        _eventFlow.send(LoginEvent.Success)
                    }
                }
            }
        }
    }

    private inline fun updateState(update: (LoginState) -> LoginState) {
        _state.value = update(_state.value)
    }
    
}