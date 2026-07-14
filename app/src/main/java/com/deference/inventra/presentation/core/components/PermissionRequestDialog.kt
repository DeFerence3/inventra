package com.deference.inventra.presentation.core.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionRequestDialog(
    onDenied: () -> Unit,
    onGrand: () -> Unit,
    permissions: String
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onDenied,
        title = { Text(text = "Permission Request") },
        text = { Text(text = "The following permission is required to function this app appropriately\n\n$permissions\n\nPlease grand those in settings.") },
        confirmButton = {
            Button(
                onClick = onGrand
            ) {
                Text(text = "Grand")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDenied
            ) {
                Text(text = "Deny")
            }
        }
    )
}