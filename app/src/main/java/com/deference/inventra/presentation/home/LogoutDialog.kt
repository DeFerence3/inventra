package com.deference.inventra.presentation.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.deference.inventra.R

@Composable
fun LogoutDialog(modifier: Modifier = Modifier,onDismiss: () -> Unit,onLogout: () -> Unit) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = stringResource(R.string.confirmation)) },
        text = {
            Text(text = stringResource(R.string.do_you_want_to_logout))
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onLogout()
                },
            ) {
                Text(text = stringResource(R.string.logout))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.dismiss))
            }
        }
    )
}