package com.deference.inventra.presentation.stockreceipt.list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.StockReceiptStatus
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.components.InputTextFieldType
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockReceiptListScreen(
    onBack: () -> Unit,
    onReceiptClick: (StockReceiptResponseBody) -> Unit,
    state: StockReceiptListState,
    eventFlow: Flow<StockReceiptListEvent>,
    onAction: (StockReceiptListActions) -> Unit
) {
    val context = LocalContext.current
    var showFilterMenu by remember { mutableStateOf(false) }

    eventFlow.ObserveEvent { event ->
        when (event) {
            is StockReceiptListEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Stock Receipts") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            StockReceiptStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.name) },
                                    onClick = {
                                        onAction(StockReceiptListActions.OnStatusFilterChange(status))
                                        showFilterMenu = false
                                    },
                                    leadingIcon = {
                                        Checkbox(
                                            checked = state.selectedStatus == status,
                                            onCheckedChange = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            InputTextField(
                text = state.searchQuery,
                onValueChange = { onAction(StockReceiptListActions.OnSearchQueryChanged(it)) },
                label = "Search",
                leadingIcon = Icons.Default.Search,
                type = InputTextFieldType.WithIcon
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                val filteredReceipts = state.receipts.filter {
                    it.transNo.contains(state.searchQuery, ignoreCase = true) ||
                            it.issueFromLocationName.contains(state.searchQuery, ignoreCase = true) ||
                            it.receiptToLocationName.contains(state.searchQuery, ignoreCase = true)
                }

                if (state.isLoading && state.receipts.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (filteredReceipts.isEmpty()) {
                    Text(
                        text = "No receipts found",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(filteredReceipts) { item ->
                            ReceiptCard(
                                item = item,
                                onClick = { onReceiptClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReceiptCard(
    item: StockReceiptResponseBody,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RectangleShape,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.transNo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.transDate.formatToString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "From: ${item.issueFromLocationName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "To: ${item.receiptToLocationName}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
