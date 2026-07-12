package com.deference.inventra.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.deference.inventra.R
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    state: LoginState,
    onAction: (LoginActions) -> Unit,
    event: Flow<LoginEvent>,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    event.ObserveEvent { event ->
        when (event) {
            is LoginEvent.Success -> onLoginSuccess()
            is LoginEvent.Error -> {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            contentAlignment = BiasAlignment(0F, -.5F)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(id = R.drawable.inventra),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputTextField(
                        label = "Username",
                        text = state.username,
                        leadingIcon = Icons.Default.Person,
                        contentType = ContentType.Username,
                        imeAction = ImeAction.Next,
                        enabled = !state.isLoading,
                        maxLine = 1
                    ) {
                        onAction(LoginActions.OnUsernameChanged(it))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    InputTextField(
                        keyboardType = KeyboardType.Password,
                        label = "Password",
                        text = state.password,
                        leadingIcon = Icons.Default.Password,
                        trailingIcon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        searchQuery = { isPasswordVisible = !isPasswordVisible },
                        contentType = ContentType.Password,
                        enabled = !state.isLoading,
                        maxLine = 1
                    ) {
                        onAction(LoginActions.OnPasswordChanged(it))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AppButton(
                            text = stringResource(id = R.string.login),
                            enabled = state.username.isNotBlank() && state.password.isNotBlank()
                        ) {
                            keyboardController?.hide()
                            onAction(LoginActions.DoLogin)
                        }
                    }
                }
            }
        }
    }
}
