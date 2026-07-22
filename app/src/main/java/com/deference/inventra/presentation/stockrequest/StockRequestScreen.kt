package com.deference.inventra.presentation.stockrequest

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.Clickable
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.components.InputTextFieldType
import com.deference.inventra.presentation.core.components.selectors.SelectionContract
import com.deference.inventra.presentation.core.components.selectors.components.SelectionConstant
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockRequestScreen(
    onBack: () -> Unit,
    state: StockRequestState,
    eventFlow: Flow<StockRequestEvent>,
    onAction: (StockRequestActions) -> Unit
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is StockRequestEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            StockRequestEvent.Success -> {
                Toast.makeText(context, "Stock request saved", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }

    var itemSelectionIndex by remember { mutableStateOf<Int?>(null) }
    var unitMenuIndex by remember { mutableStateOf<Int?>(null) }

    val receiptLocationLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<Location>(
            selectionConst = SelectionConstant.LOCATION,
            head = "Select Receipt Location"
        ),
        onResult = { result ->
            if (result != null) {
                onAction(StockRequestActions.SelectReceiptLocation(result))
            }
        }
    )

    val transferLocationLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<Location>(
            selectionConst = SelectionConstant.LOCATION,
            head = "Select Transfer Location"
        ),
        onResult = { result ->
            if (result != null) {
                onAction(StockRequestActions.SelectTransferLocation(result))
            }
        }
    )

    val itemLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<SearchItem>(
            selectionConst = SelectionConstant.ITEM,
            head = "Select Item"
        ),
        onResult = { result ->
            val index = itemSelectionIndex
            if (result != null && index != null) {
                onAction(StockRequestActions.SelectItem(index, result))
            }
            itemSelectionIndex = null
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Stock Request") },
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
                onClick = { onAction(StockRequestActions.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Master Values",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                InputTextField(
                    text = state.requestNo,
                    onValueChange = { },
                    label = "Request No",
                    enabled = false,
                    type = InputTextFieldType.Outlined
                )

                InputTextField(
                    text = state.requestDate.formatToString(),
                    onValueChange = { },
                    label = "Request Date",
                    enabled = false,
                    type = InputTextFieldType.Outlined
                )

                Clickable(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.receiptLocation?.name ?: "",
                    label = "Receipt Location",
                    error = state.receiptLocationError,
                    onClick = {
                        receiptLocationLauncher.launch(Unit)
                    }
                )

                Clickable(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.transferLocation?.name ?: "",
                    label = "Transfer Location",
                    error = state.transferLocationError,
                    onClick = {
                        transferLocationLauncher.launch(Unit)
                    }
                )

                InputTextField(
                    text = state.remarks,
                    onValueChange = { onAction(StockRequestActions.OnRemarksChanged(it)) },
                    label = "Remarks",
                    type = InputTextFieldType.Outlined,
                    maxLine = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { onAction(StockRequestActions.AddItem) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Item",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                state.items.forEachIndexed { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Item #${index + 1}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (state.items.size > 1) {
                                IconButton(
                                    onClick = { onAction(StockRequestActions.RemoveItem(index)) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Item",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        Clickable(
                            modifier = Modifier.fillMaxWidth(),
                            value = item.itemName,
                            label = "Select Item",
                            error = item.itemError,
                            onClick = {
                                itemSelectionIndex = index
                                itemLauncher.launch(Unit)
                            }
                        )

                        if (item.itemGroup.isNotBlank() || item.unitName.isNotBlank()) {
                            Text(
                                text = listOf(item.itemGroup, item.unitName)
                                    .filter { it.isNotBlank() }
                                    .joinToString(" / "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Clickable(
                                modifier = Modifier.fillMaxWidth(),
                                value = item.unit?.name ?: "",
                                label = "Unit",
                                error = item.unitError,
                                onClick = if (item.units.isNotEmpty()) {
                                    { unitMenuIndex = index }
                                } else {
                                    null
                                }
                            )
                            DropdownMenu(
                                expanded = unitMenuIndex == index,
                                onDismissRequest = { unitMenuIndex = null }
                            ) {
                                item.units.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit.name) },
                                        onClick = {
                                            onAction(StockRequestActions.SelectUnit(index, unit))
                                            unitMenuIndex = null
                                        }
                                    )
                                }
                            }
                        }

                        InputTextField(
                            text = item.requestedQty,
                            onValueChange = { onAction(StockRequestActions.OnItemQtyChanged(index, it)) },
                            label = "Qty",
                            keyboardType = KeyboardType.Number,
                            type = InputTextFieldType.Outlined,
                            error = item.requestedQtyError
                        )

                        InputTextField(
                            text = item.remarks,
                            onValueChange = { onAction(StockRequestActions.OnItemRemarksChanged(index, it)) },
                            label = "Remarks",
                            type = InputTextFieldType.Outlined
                        )
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
