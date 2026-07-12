package com.deference.inventra.presentation.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.deference.inventra.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    text: String,
    searchQuery: () -> Unit = {},
    label: String = stringResource(R.string.text_label),
    leadingIcon: ImageVector = Icons.Default.Email,
    trailingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
    maxLine: Int = 3,
    contentType: ContentType = ContentType("Text"),
    type: InputTextFieldType = InputTextFieldType.WithIcon,
    error: String? = null,
    onValueChange: (String) -> Unit,
) {
    when (type) {
        InputTextFieldType.Classic -> TextField(
            visualTransformation = visualTransformation,
            value = text,
            label = { Text(text = label) },
            readOnly = !enabled,
            modifier = modifier.fillMaxWidth()
            ,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.extraSmall,
            placeholder = { Text(text = label) },
            maxLines = maxLine,
            isError = error != null,
            supportingText = error?.let { { Text(text = it) } }
        )

        InputTextFieldType.Outlined -> OutlinedTextField(
            visualTransformation = visualTransformation,
            value = text,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            label = { Text(label) },
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            readOnly = !enabled,
            shape = MaterialTheme.shapes.small,
            maxLines = maxLine,
            isError = error != null,
            supportingText = error?.let { { Text(text = it) } }
        )

        InputTextFieldType.WithIcon -> OutlinedTextField(
            visualTransformation = visualTransformation,
            value = text,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth()
                .semantics {
                    this.contentType = contentType
                },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Icon",
                )
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    IconButton(onClick = searchQuery) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Icon",
                        )
                    }
                }
            },
            label = { Text(label) },
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            /*colors = TextFieldDefaults.colors(

                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),*/
            enabled = enabled,
            shape = MaterialTheme.shapes.small,
            maxLines = maxLine,
            isError = error != null,
            supportingText = error?.let { { Text(text = it) } }
        )

        InputTextFieldType.IconClickable -> OutlinedTextField(
            visualTransformation = visualTransformation,
            value = text,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = {
                IconButton(onClick = searchQuery) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = "Icon",
                    )
                }
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    IconButton(onClick = searchQuery) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Icon",
                        )
                    }
                }
            },
            label = { Text(label) },
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            /*colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),*/
            enabled = enabled,
            shape = MaterialTheme.shapes.small,
            maxLines = maxLine,
            isError = error != null,
            supportingText = error?.let { { Text(text = it) } }
        )
    }
}

@Composable
fun Clickable(
    modifier: Modifier = Modifier,
    value: String,
    onClick: (() -> Unit?)?,
    label: String,
    error: String? = null
) {
    Clickable(
        modifier = modifier,
        value = value,
        onClick = onClick,
        label = label,
        isError = error != null,
        supportingText = error,
    )
}

@Composable
fun Clickable(
    modifier: Modifier = Modifier,
    value: String,
    onClick: (() -> Unit?)?,
    label: String,
    supportingText: String? = null,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        readOnly = true,
        onValueChange = {},
        modifier = modifier
            .clickable(enabled = onClick != null){
                onClick?.invoke()
            },
        label = { Text(text = label, maxLines = 1) },
        colors = OutlinedTextFieldDefaults.colors().copy(
            disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
            disabledIndicatorColor = if (!isError) OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor else OutlinedTextFieldDefaults.colors().errorIndicatorColor,
            disabledLeadingIconColor = OutlinedTextFieldDefaults.colors().focusedLeadingIconColor,
            disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor,
            disabledLabelColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor,
            disabledSupportingTextColor = if (!isError) OutlinedTextFieldDefaults.colors().unfocusedSupportingTextColor else OutlinedTextFieldDefaults.colors().errorSupportingTextColor,
        ),
        enabled = false,
        supportingText = supportingText?.let { { Text(text = it, maxLines = 1) } },
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
fun ClickablePrev() {
    Column() {
        Clickable(
            value = "123",
            onClick = { },
            label = "Clickable"
        )
        InputTextField(
            text = "Text",
            onValueChange = { },
            label = "Remarks",
            type = InputTextFieldType.Outlined
        )
    }
}

enum class InputTextFieldType {
    Classic, Outlined, WithIcon, IconClickable
}