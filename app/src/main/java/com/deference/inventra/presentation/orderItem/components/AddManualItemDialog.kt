package com.deference.inventra.presentation.orderItem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.item.SearchItem

@Composable
fun AddManualItemDialog(
    searchResults: List<SearchItem>,
    isSearching: Boolean,
    onSearch: (String) -> Unit,
    onDismiss: () -> Unit,
    onAdd: (SearchItem, Double, Double) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedItem by remember { mutableStateOf<SearchItem?>(null) }
    var qtyText by remember { mutableStateOf("1.0") }
    var rateText by remember { mutableStateOf("0.0") }

    val qty = qtyText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val netAmount = qty * rate
    val vatAmount = netAmount * 0.05
    val grossAmount = netAmount + vatAmount
    val canAdd = selectedItem != null && qtyText.toDoubleOrNull() != null && rateText.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Non-GRN Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (selectedItem == null) Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search Item") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(onClick = { onSearch(searchQuery) }) {
                        Icon(Icons.Default.Search,null)
                    }
                }

                if (isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (searchResults.isNotEmpty() && selectedItem == null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { item ->
                            ListItem(
                                headlineContent = { Text(item.itemName) },
                                supportingContent = item.itemCode?.let{ { Text(item.itemCode) }},
                                modifier = Modifier.clickable {
                                    selectedItem = item
                                    //rateText = item.pr.toString()
                                }
                            )
                        }
                    }
                }

                selectedItem?.let { item ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Text("${item.itemName} (${item.itemCode})", style = MaterialTheme.typography.titleMedium)
                        IconButton(
                            onClick = { selectedItem = null },
                        ) {
                            Icon(Icons.Default.RestartAlt,null)
                        }
                    }
                    OutlinedTextField(
                        value = qtyText,
                        onValueChange = { qtyText = it },
                        label = { Text("Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rateText,
                        onValueChange = { rateText = it },
                        label = { Text("Rate") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AmountRow("Net", netAmount.asAmount())
                    AmountRow("VAT (5%)", vatAmount.asAmount())
                    AmountRow("Gross", grossAmount.asAmount())
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { selectedItem?.let { onAdd(it, qty, rate) } },
                enabled = canAdd
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
