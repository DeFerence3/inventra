package com.deference.inventra.presentation.orderItem

import android.widget.Toast
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.purchase.OrderItem
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.ErrorDialog
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderItemListScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    state: OrderItemListState,
    eventFlow: Flow<OrderItemListEvent>,
    onAction: (OrderItemListActions) -> Unit,
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is OrderItemListEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            OrderItemListEvent.Success -> {
                Toast.makeText(context, "GRN Saved Successfully", Toast.LENGTH_SHORT).show()
                onSave()
            }
        }
    }

    if (state.error != null) {
        ErrorDialog(
            onDismiss = {
                onAction(OrderItemListActions.DismissError)
            },
            error = state.error
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Items from POs") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            AppButton(
                text = "Save",
                enabled = (state.isLoading || state.items.isEmpty() || state.error != null).not(),
                onClick = { onAction(OrderItemListActions.SaveGrn) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.items.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.items.isEmpty()) {
                Text(
                    text = "No items found",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items) { item ->
                        OrderItemCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(item: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = "${item.itemCode} - ${item.itemName}",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Qty",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${item.requiredQty} ${item.unitName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                VerticalDivider()
                Column {
                    Text(
                        text = "Rate",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.pricePerUnit.asAmount(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                VerticalDivider()
                Column {
                    Text(
                        text = "Net",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.netAmount.asAmount(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                VerticalDivider()
                Column {
                    Text(
                        text = "Vat",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.taxAmount.asAmount(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                VerticalDivider()
                Column {
                    Text(
                        text = "Gross",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.grossAmount.asAmount(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
