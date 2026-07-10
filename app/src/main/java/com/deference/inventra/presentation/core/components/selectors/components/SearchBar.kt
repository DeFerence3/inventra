package com.deference.inventra.presentation.core.components.selectors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deference.inventra.R
import com.deference.inventra.presentation.core.keyboardAsState

@Composable
fun SearchBar(
    hint: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    height: Dp = 45.dp,
    elevation: Dp = 3.dp,
    onSearchClicked: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    immediateSearch: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboard by keyboardAsState()

    Column {
        Row(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .shadow(elevation = elevation, shape = RectangleShape)
                .background(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                    shape = RectangleShape
                )
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(.5f))
                .clickable { onSearchClicked() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                cursorBrush = if (keyboard.name == "Opened") {
                    SolidColor(MaterialTheme.colorScheme.onSurface)
                } else {
                    SolidColor(Color.Unspecified)
                },
                modifier = modifier
                    .weight(5f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = text,
                onValueChange = {
                    text = it
                    if (immediateSearch)
                    onTextChange(it.text)
                },
                interactionSource = remember { MutableInteractionSource() },
                enabled = isEnabled,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                ),
                decorationBox = { innerTextField ->
                    if (text.text.isEmpty()) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    onTextChange(text.text)
                }),
                singleLine = true,
            )
            Box(
                modifier = modifier
                    .weight(1f)
                    .size(45.dp)
                    .background(color = Color.Transparent, shape = CircleShape)
                    .clickable {
                        if (text.text.isNotEmpty() && immediateSearch) {
                            text = TextFieldValue(text = "")
                            onTextChange("")
                        } else if (!immediateSearch && text.text.isNotEmpty()) {
                            onTextChange(text.text)
                        }
                    },
            ) {
                if (text.text.isNotEmpty() && immediateSearch) {
                    Icon(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface.copy(.5f)
                    )
                } else {
                    Icon(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface.copy(.5f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}