package com.deference.inventra.presentation.stockreceipt.details

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.domain.model.stock.receipt.Item
import com.deference.inventra.presentation.approvals.approve.components.DetailRow
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.AppButtonType
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockReceiptDetailScreen(
    onBack: () -> Unit,
    state: StockReceiptDetailState,
    eventFlow: Flow<StockReceiptDetailEvent>,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is StockReceiptDetailEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            StockReceiptDetailEvent.Success -> {
                Toast.makeText(context, "Stock receipt updated", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }

    val receipt = state.receipt
    val isPending = receipt != null && receipt.status == 1

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Receipt Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (isPending) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppButton(
                        modifier = Modifier.weight(1f),
                        text = "Decline",
                        onClick = onDecline,
                        type = AppButtonType.Outlined,
                        enabled = !state.isLoading
                    )
                    AppButton(
                        modifier = Modifier.weight(1f),
                        text = "Accept",
                        onClick = onAccept,
                        enabled = !state.isLoading
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (receipt != null) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    DetailRow("Trans No", receipt.transNo)
                    DetailRow("Date", receipt.transDate.formatToString())
                    DetailRow("From Location", receipt.issueFromLocationName)
                    DetailRow("To Location", receipt.receiptToLocationName)
                    DetailRow("Remarks", receipt.remarks.ifEmpty { "N/A" })

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    receipt.items.forEach { item ->
                        ReceiptItemCard(item = item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    val statusText = when (receipt.status) {
                        1 -> "Pending"
                        2 -> "Accepted"
                        3 -> "Declined"
                        else -> "Unknown"
                    }
                    Text(
                        "Status: $statusText",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (!state.isLoading) {
                Text(
                    text = state.error ?: "Details not loaded",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun ReceiptItemCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${item.itemCode} - ${item.itemName}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Issued Qty", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${item.issueQty} ${item.unitName}", style = MaterialTheme.typography.bodySmall)
                }
                Column {
                    Text(text = "Requested Qty", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${item.requestedQty} ${item.unitName}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
