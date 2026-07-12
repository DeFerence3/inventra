package com.deference.inventra.presentation.purchaseOrder

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deference.inventra.domain.model.purchase.PurchaseOrder
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.AppButtonType
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderListScreen(
    onContinue: (List<String>?) -> Unit,
    onBack: () -> Unit,
    state: PurchaseOrderState,
    eventFlow: Flow<PurchaseOrderEvent>,
    onAction: (PurchaseOrderActions) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    eventFlow.ObserveEvent { event ->
        when (event) {
            is PurchaseOrderEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= state.purchaseOrders.size - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !state.isLoading && !state.isLastPage) {
            onAction(PurchaseOrderActions.LoadNextPage)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Select Purchase Orders") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(state.selectedPOUUIDs.isNotEmpty()) {
                    AppButton(
                        text = "Continue",
                        enabled = state.selectedPOUUIDs.isNotEmpty(),
                        onClick = { onContinue(state.selectedPOUUIDs.toList()) }
                    )
                }
                AnimatedVisibility(!state.selectedPOUUIDs.isNotEmpty()) {
                    AppButton(
                        text = "Skip PO Selection",
                        type = AppButtonType.Text,
                        onClick = { onContinue(null) }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.purchaseOrders) { po ->
                PurchaseOrderItem(
                    purchaseOrder = po,
                    isSelected = state.selectedPOUUIDs.contains(po.uuid),
                    onToggle = {
                        onAction(PurchaseOrderActions.OnTogglePO(po.uuid))
                    }
                )
            }
            if (state.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun PurchaseOrderItem(
    purchaseOrder: PurchaseOrder,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = if (isSelected) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        } else {
            CardDefaults.cardColors()
        },
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Order No: ${purchaseOrder.transNo}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Date: ${purchaseOrder.transDate}", style = MaterialTheme.typography.bodyMedium)
            }
            Checkbox(checked = isSelected, onCheckedChange = { onToggle() })
        }
    }
}
