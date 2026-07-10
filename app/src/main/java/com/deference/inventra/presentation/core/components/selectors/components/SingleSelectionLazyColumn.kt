package com.deference.inventra.presentation.core.components.selectors.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deference.inventra.R
import com.deference.inventra.presentation.core.components.selectors.SelectAnyProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleSelectionLazyColumn(
    items: List<SelectAnyProvider>,
    onSelectionClick: (SelectAnyProvider) -> Unit,
    isStickHeaderVisible: Boolean = false
) {

    val allItem = items.find { it.title == "ALL" }
    val otherItems = items.filter { it.title != "ALL" }

    val groupedItems = otherItems.groupBy { it.title.first().uppercaseChar() }
    val sortedGroups = groupedItems.toSortedMap()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {

        allItem?.let {
            item {
                SingleSelectionItem(item = it, onSelectionClick = onSelectionClick)
            }
        }

        sortedGroups.forEach { (letter, groupItems) ->
            stickyHeader {
                if (isStickHeaderVisible) {
                    Surface(
                        Modifier.fillMaxWidth(),
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            modifier = Modifier.padding(14.dp),
                            text = letter.toString(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            items(
                items = groupItems,
                itemContent = { item ->
                    SingleSelectionItem(item = item, onSelectionClick = onSelectionClick)
                }
            )
        }
    }
}


@Composable
fun SingleSelectionItem(
    onSelectionClick: (SelectAnyProvider) -> Unit,
    item: SelectAnyProvider
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable {
                onSelectionClick(item)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(item.image != null){
            Box(
                modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .size(45.dp))
            {
                Image(
                    modifier = Modifier,
//                        .background(Color.White)
//                        .clip(RoundedCornerShape(12.dp))
//                        .size(45.dp),
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
                        text = item.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        //overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                    item.subTitle?.let {
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

// Shimmer View

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleSelectionLazyColumnShimmer() {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        stickyHeader {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                shadowElevation = 2.dp,
                tonalElevation = 1.dp,
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        //.shimmerEffect(),
                )
            }
        }

        items(10) {
            SingleSelectionItemShimmer()
        }
    }
}

@Composable
fun SingleSelectionItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier= Modifier
                .padding(8.dp)
                .size(60.dp)
                //.shimmerEffect()
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column() {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(16.dp)
                            .fillMaxWidth()
                            //.shimmerEffect(),
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(16.dp)
                            .fillMaxWidth()
                            //.shimmerEffect(),
                    )
                }
            }
        }

    }
}
