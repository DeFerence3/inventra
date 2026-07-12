@file:Suppress("DEPRECATION")

package com.deference.inventra.presentation.purchaserequest

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import com.deference.inventra.core.utils.now
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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseRequestScreen(
    onBack: () -> Unit,
    state: PurchaseRequestState,
    eventFlow: Flow<PurchaseRequestEvent>,
    onAction: (PurchaseRequestActions) -> Unit
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is PurchaseRequestEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            PurchaseRequestEvent.Success -> {
                Toast.makeText(context, "Purchase request saved", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }

    var locationSelectionIndex by remember { mutableStateOf<Int?>(null) }
    var itemSelectionIndex by remember { mutableStateOf<Int?>(null) }
    var expectedDateSelectionIndex by remember { mutableStateOf<Int?>(null) }
    var unitMenuIndex by remember { mutableStateOf<Int?>(null) }
    var showExpectedDatePicker by remember { mutableStateOf(false) }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<Location>(
            selectionConst = SelectionConstant.LOCATION,
            head = "Select Location"
        ),
        onResult = { result ->
            val index = locationSelectionIndex
            if (result != null && index != null) {
                onAction(PurchaseRequestActions.SelectLocation(index, result))
            }
            locationSelectionIndex = null
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
                onAction(PurchaseRequestActions.SelectItem(index, result))
            }
            itemSelectionIndex = null
        }
    )

    if (showExpectedDatePicker) {
        val index = expectedDateSelectionIndex
        val selectedDate = index?.let { state.items.getOrNull(it)?.expectedDate } ?: LocalDateTime.now()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toInstant(TimeZone.UTC).toEpochMilliseconds()
        )
        DatePickerDialog(
            onDismissRequest = {
                showExpectedDatePicker = false
                expectedDateSelectionIndex = null
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedIndex = expectedDateSelectionIndex
                        val millis = datePickerState.selectedDateMillis
                        if (selectedIndex != null && millis != null) {
                            @Suppress("DEPRECATION")
                            val pickedDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC)
                            val currentTime = LocalDateTime.now()
                            @Suppress("DEPRECATION")
                            onAction(
                                PurchaseRequestActions.OnItemExpectedDateChanged(
                                    selectedIndex,
                                    LocalDateTime(
                                        year = pickedDate.year,
                                        monthNumber = pickedDate.monthNumber,
                                        dayOfMonth = pickedDate.dayOfMonth,
                                        hour = currentTime.hour,
                                        minute = currentTime.minute,
                                        second = currentTime.second,
                                        nanosecond = currentTime.nanosecond
                                    )
                                )
                            )
                        }
                        showExpectedDatePicker = false
                        expectedDateSelectionIndex = null
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExpectedDatePicker = false
                        expectedDateSelectionIndex = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Purchase Request") },
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
                onClick = { onAction(PurchaseRequestActions.Submit) },
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
                    onValueChange = { onAction(PurchaseRequestActions.OnRequestNoChanged(it)) },
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

                InputTextField(
                    text = state.internalNotes,
                    onValueChange = { onAction(PurchaseRequestActions.OnInternalNotesChanged(it)) },
                    label = "Internal Notes",
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
                        onClick = { onAction(PurchaseRequestActions.AddItem) }
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
                                    onClick = { onAction(PurchaseRequestActions.RemoveItem(index)) },
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
                            value = item.location?.name ?: "",
                            label = "Location",
                            error = item.locationError,
                            onClick = {
                                locationSelectionIndex = index
                                locationLauncher.launch(Unit)
                            }
                        )

                        Clickable(
                            modifier = Modifier.fillMaxWidth(),
                            value = item.expectedDate.formatToString(),
                            label = "Expected Date",
                            onClick = {
                                expectedDateSelectionIndex = index
                                showExpectedDatePicker = true
                            }
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                            ) {
                                Box(modifier = Modifier.weight(0.35f)) {
                                    Clickable(
                                        value = item.itemCode,
                                        label = "Code",
                                        isError = item.itemError != null,
                                        onClick = {
                                            itemSelectionIndex = index
                                            itemLauncher.launch(Unit)
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(0.65f)) {
                                    Clickable(
                                        value = item.itemName,
                                        label = "Item",
                                        isError = item.itemError != null,
                                        onClick = {
                                            itemSelectionIndex = index
                                            itemLauncher.launch(Unit)
                                        }
                                    )
                                }
                            }
                            item.itemError?.let {
                                Text(
                                    text = it,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OutlinedTextFieldDefaults.colors().errorSupportingTextColor
                                )
                            }
                        }

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
                                            onAction(PurchaseRequestActions.SelectUnit(index, unit))
                                            unitMenuIndex = null
                                        }
                                    )
                                }
                            }
                        }

                        InputTextField(
                            text = item.requestedQty,
                            onValueChange = { onAction(PurchaseRequestActions.OnItemQtyChanged(index, it)) },
                            label = "Req Qty",
                            keyboardType = KeyboardType.Number,
                            type = InputTextFieldType.Outlined,
                            error = item.requestedQtyError
                        )

                        InputTextField(
                            text = item.remarks,
                            onValueChange = { onAction(PurchaseRequestActions.OnItemRemarksChanged(index, it)) },
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
