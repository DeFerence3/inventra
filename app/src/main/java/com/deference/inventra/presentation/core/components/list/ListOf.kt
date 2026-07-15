package com.deference.inventra.presentation.core.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListOf(
    items: List<T>,
    listItem: @Composable LazyItemScope.(item: T) -> Unit,
    modifier: Modifier = Modifier,
    groupBy: ((T) -> String)? = null,
    state: LazyListState = rememberLazyListState(),
    additionalItem: @Composable (LazyItemScope.() -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (groupBy == null) {
            items(
                items = items,
                itemContent = listItem
            )
        }else {
            val groupedItems = items.groupBy(keySelector=groupBy)
            val sortedGroups = groupedItems.toSortedMap()
            sortedGroups.forEach { (letter, groupItems) ->
                stickyHeader {
                    Text(
                        modifier = Modifier.padding(14.dp),
                        text = letter.toString(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                items(
                    items = groupItems,
                    itemContent = listItem
                )
            }
        }
        if (additionalItem != null) {
            item(
                content = additionalItem
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfPrev() {
    ListOf(
        items = listOf(1,2,3,4),
        listItem = {
            ListItem(
                header = it.toString(),
                supportingText = it.toString(),
            )
        }
    )
}
