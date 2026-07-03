package com.deference.inventra.presentation.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    type: AppButtonType = AppButtonType.Filled,
    fillMaxWidth: Boolean = true,
    onClick: () -> Unit
) {
    val commonModifier = if (fillMaxWidth){
        modifier.fillMaxWidth()
    }else modifier
    when (type) {
        AppButtonType.Filled -> Button(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = RectangleShape

        ) {
            ButtonContent(text = text)
        }
        AppButtonType.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = commonModifier,
            enabled = enabled,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
            ),
        ) {
            ButtonContent(text = text)
        }
        AppButtonType.Text -> {
            TextButton(
                onClick = onClick,
                modifier = commonModifier,
                enabled = enabled,
                shape = RectangleShape
            ) {
                ButtonContent(text = text)
            }
        }
    }
}

@Composable
private fun ButtonContent(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(8.dp),
    )
}

enum class AppButtonType {
    Filled, Outlined, Text
}

@Preview(showBackground = true)
@Composable
fun PrevButton() {
    AppButton(
        text = "Text",
        onClick = { },
        type = AppButtonType.Text
    )
}