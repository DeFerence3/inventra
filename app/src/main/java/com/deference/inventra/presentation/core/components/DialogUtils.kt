package com.deference.inventra.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ErrorDialog(error: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 4.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Error!!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(18.dp))
                SelectionContainer(
                    modifier = Modifier
                        .widthIn(min = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = error,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(8.dp),
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                AppButton(
                    modifier = Modifier.align(Alignment.End),
                    text = "Dismiss",
                    onClick = onDismiss,
                    type = AppButtonType.Text,
                    fillMaxWidth = false,
                )
            }
        }
    }
}
