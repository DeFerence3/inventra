package com.deference.inventra.presentation.core.components.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deference.inventra.R

@Composable
fun ListItem(
    header: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    image: Painter = painterResource(R.drawable.img_placeholder)
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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

@Composable
fun ListItem2(
    header: String,
    image: String?,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(image != null){
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .size(45.dp))
            {
                Image(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.img_placeholder),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )
            }
        } else {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .size(45.dp),
                painter = painterResource(id = R.drawable.img_placeholder),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column{
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = header,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 3
                    )
                    supportingText?.let {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = it,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
    HorizontalDivider(modifier = Modifier.padding(4.dp))
}
