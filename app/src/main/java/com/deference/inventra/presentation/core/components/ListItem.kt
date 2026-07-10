package com.deference.inventra.presentation.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deference.inventra.R

@Composable
fun ListItem(
    header: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    image: Painter = painterResource(R.drawable.img_placeholder),
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = image,
                contentDescription = "$header Icon",
                modifier = Modifier.size(50.dp).padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(text = header, style = MaterialTheme.typography.titleMedium)
                supportingText?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}