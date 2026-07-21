package com.deference.inventra.presentation.spotcheck

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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

    var locationSelectionIndex by remember { mutableStateOf<Int?>(null) }
    var itemSelectionIndex by remember { mutableStateOf<Int?>(null) }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = SelectionContract<Location>(
            selectionConst = SelectionConstant.LOCATION,
            head = "Select Location"
        ),
        onResult = { result ->
            val index = locationSelectionIndex
            if (result != null && index != null) {
                onAction(SpotCheckActions.SelectLocation(index, result.name))
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
                onAction(SpotCheckActions.SelectItem(index, result))
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
                    text = state.spotCheckNo,
                    onValueChange = { onAction(SpotCheckActions.OnSpotCheckNoChanged(it)) },
                    label = "Spot Check No",
                    type = InputTextFieldType.Outlined,
                    enabled = false
                )

                InputTextField(
                    text = state.spotCheckDate.formatToString(),
                    onValueChange = { /*onAction(SpotCheckActions.OnSpotCheckDateChanged(it))*/ },
                    label = "Spot Check Date",
                    type = InputTextFieldType.Outlined,
                    enabled = false
                )

                InputTextField(
                    text = state.internalNotes,
                    onValueChange = { onAction(SpotCheckActions.OnInternalNotesChanged(it)) },
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
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Clickable(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = item.location,
                                label = "Location",
                                error = item.locationError,
                                onClick = {
                                    locationSelectionIndex = index
                                    locationLauncher.launch(Unit)
                                }
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)) {
                                Box(modifier = Modifier.weight(0.3f)) {
                                    Clickable(
                                        modifier = Modifier,
                                        value = item.itemCode,
                                        label = "Code",
                                        isError = item.itemError != null,
                                        onClick = {
                                            itemSelectionIndex = index
                                            itemLauncher.launch(Unit)
                                        }
                                    )
                                }
                                Box(modifier = Modifier.weight(0.7f)) {
                                    Clickable(
                                        modifier = Modifier,
                                        value = item.itemName,
                                        label = "Item Name",
                                        isError = item.itemError != null,
                                        onClick = {
                                            itemSelectionIndex = index
                                            itemLauncher.launch(Unit)
                                        }
                                    )
                                }
                            }
                            item.itemError?.let{
                                val typography = MaterialTheme.typography
                                val bodySmall = typography.bodySmall
                                Text(text = it, maxLines = 1, style = bodySmall, color = OutlinedTextFieldDefaults.colors().errorSupportingTextColor)
                            }
                        }

                        InputTextField(
                            text = item.physicalQty,
                            onValueChange = { onAction(SpotCheckActions.OnItemQtyChanged(index, it)) },
                            label = "Physical Qty",
                            keyboardType = KeyboardType.Number,
                            type = InputTextFieldType.Outlined,
                            error = item.physicalQtyError
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




