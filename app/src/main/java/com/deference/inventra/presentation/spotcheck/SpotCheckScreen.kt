package com.deference.inventra.presentation.spotcheck

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.components.InputTextFieldType
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotCheckScreen(
    onBack: () -> Unit,
    state: SpotCheckState,
    eventFlow: Flow<SpotCheckEvent>,
    onAction: (SpotCheckActions) -> Unit
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is SpotCheckEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            SpotCheckEvent.Success -> {
                Toast.makeText(context, "Spot check submitted successfully", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }

    // Location Selector Dialog
    state.showLocationSelectorForIndex?.let { index ->
        AlertDialog(
            onDismissRequest = { onAction(SpotCheckActions.CloseLocationSelector) },
            title = { Text("Select Location") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(400.dp)
                ) {
                    InputTextField(
                        text = state.locationSearchQuery,
                        onValueChange = { onAction(SpotCheckActions.OnLocationSearchQueryChanged(it)) },
                        label = "Search Location",
                        leadingIcon = Icons.Default.Search,
                        type = InputTextFieldType.WithIcon
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.isSearchingLocations) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.locations) { location ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onAction(SpotCheckActions.SelectLocation(index, location.name)) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Text(
                                        text = location.name,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onAction(SpotCheckActions.CloseLocationSelector) }) {
                    Text("Close")
                }
            }
        )
    }

    // Item Selector Dialog
    state.showItemSelectorForIndex?.let { index ->
        AlertDialog(
            onDismissRequest = { onAction(SpotCheckActions.CloseItemSelector) },
            title = { Text("Select Item") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(400.dp)
                ) {
                    InputTextField(
                        text = state.itemSearchQuery,
                        onValueChange = { onAction(SpotCheckActions.OnItemSearchQueryChanged(it)) },
                        label = "Search Item",
                        leadingIcon = Icons.Default.Search,
                        type = InputTextFieldType.WithIcon
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.isSearchingItems) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.searchedItems) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onAction(SpotCheckActions.SelectItem(index, item)) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = item.itemName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                        item.itemCode?.let {
                                            Text(text = "Code: $it", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onAction(SpotCheckActions.CloseItemSelector) }) {
                    Text("Close")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Spot Check") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            AppButton(
                text = "Submit",
                onClick = { onAction(SpotCheckActions.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.padding(16.dp)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Master Values",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                InputTextField(
                    text = state.spotCheckNo,
                    onValueChange = { onAction(SpotCheckActions.OnSpotCheckNoChanged(it)) },
                    label = "Spot Check No",
                    type = InputTextFieldType.Outlined
                )

                InputTextField(
                    text = state.spotCheckDate.formatToString(),
                    onValueChange = { /*onAction(SpotCheckActions.OnSpotCheckDateChanged(it))*/ },
                    label = "Spot Check Date",
                    type = InputTextFieldType.Outlined
                )

                InputTextField(
                    text = state.internalNotes,
                    onValueChange = { onAction(SpotCheckActions.OnInternalNotesChanged(it)) },
                    label = "Internal Notes",
                    type = InputTextFieldType.Outlined,
                    maxLine = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        onClick = { onAction(SpotCheckActions.AddItem) }
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    onClick = { onAction(SpotCheckActions.RemoveItem(index)) },
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

                        // Clicking this opens Location Selector dialog
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAction(SpotCheckActions.OpenLocationSelector(index)) }
                        ) {
                            InputTextField(
                                text = item.location,
                                onValueChange = {},
                                label = "Location",
                                type = InputTextFieldType.Outlined,
                                enabled = false
                            )
                        }

                        // Clicking this opens Item Selector dialog
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAction(SpotCheckActions.OpenItemSelector(index)) }
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        text = item.itemCode,
                                        onValueChange = {},
                                        label = "Item Code",
                                        type = InputTextFieldType.Outlined,
                                        enabled = false
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        text = item.itemName,
                                        onValueChange = {},
                                        label = "Item Name",
                                        type = InputTextFieldType.Outlined,
                                        enabled = false
                                    )
                                }
                            }
                        }

                        InputTextField(
                            text = item.physicalQty,
                            onValueChange = { onAction(SpotCheckActions.OnItemQtyChanged(index, it)) },
                            label = "Physical Qty",
                            keyboardType = KeyboardType.Number,
                            type = InputTextFieldType.Outlined
                        )

                        InputTextField(
                            text = item.remarks,
                            onValueChange = { onAction(SpotCheckActions.OnItemRemarksChanged(index, it)) },
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
