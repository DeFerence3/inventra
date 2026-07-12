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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.purchase.OrderItem
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.ErrorDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.presentation.core.components.selectors.SelectionContract
import com.deference.inventra.presentation.core.components.selectors.components.SelectionConstant
import com.deference.inventra.presentation.core.utils.ObserveEvent
import com.deference.inventra.presentation.orderItem.components.AddManualItemDialog
import com.deference.inventra.presentation.orderItem.components.DeliveryChallanDialog
import com.deference.inventra.presentation.orderItem.components.EditOrderItemDialog
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
    var selectedItem by remember { mutableStateOf<OrderItem?>(null) }
    var showDeliveryChallanDialog by remember { mutableStateOf(false) }
    var selectedItemForAdd by remember { mutableStateOf<SearchItem?>(null) }
    val context = LocalContext.current

    val itemLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<SearchItem>(
            selectionConst = SelectionConstant.ITEM,
            head = "Select Item"
        ),
        onResult = { result ->
            if (result != null) {
                selectedItemForAdd = result
            }
        }
    )

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

    state.error?.let { error ->
        ErrorDialog(
            error = error,
            onDismiss = { onAction(OrderItemListActions.DismissError) }
        )
    }

    selectedItem?.let { item ->
        EditOrderItemDialog(
            item = item,
            onDismiss = { selectedItem = null },
            onSave = { qty, rate ->
                onAction(
                    OrderItemListActions.UpdateItemAmounts(
                        purchaseOrderItemUuid = item.purchaseOrderItemUuid,
                        qty = qty,
                        rate = rate,
                    )
                )
                selectedItem = null
            }
        )
    }

    if (showDeliveryChallanDialog) {
        DeliveryChallanDialog(
            onDismiss = { showDeliveryChallanDialog = false },
            onConfirm = { challanNo, challanDate ->
                onAction(
                    OrderItemListActions.SaveGrn(
                        deliveryChallanNo = challanNo,
                        deliveryChallanDate = challanDate
                    )
                )
                showDeliveryChallanDialog = false
            }
        )
    }

    selectedItemForAdd?.let { item ->
        AddManualItemDialog(
            item = item,
            onDismiss = { selectedItemForAdd = null },
            onAdd = { manualItem, qty, rate ->
                onAction(OrderItemListActions.AddManualItem(manualItem.toItem(), qty, rate))
                selectedItemForAdd = null
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Order Items") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { itemLauncher.launch(Unit) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item")
                    }
                }
            )
        },
        bottomBar = {
            AppButton(
                text = "Save GRN",
                enabled = (state.isLoading || state.items.isEmpty() || state.error != null).not(),
                onClick = { showDeliveryChallanDialog = true }
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
                    text = "No items",
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
                        OrderItemCard(item = item, onClick = { selectedItem = item })
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    item: OrderItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RectangleShape,
        onClick = onClick
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
                        text = "VAT(${item.taxPercentage}%)",
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
