package com.deference.inventra.presentation.orderItem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.purchase.OrderItem

@Composable
fun EditOrderItemDialog(
    item: OrderItem,
    onDismiss: () -> Unit,
    onSave: (qty: Double, rate: Double) -> Unit,
) {
    var qtyText by remember(item.purchaseOrderItemUuid) { mutableStateOf(item.requiredQty.toString()) }
    var rateText by remember(item.purchaseOrderItemUuid) { mutableStateOf(item.pricePerBaseUnit.toString()) }

    val qty = qtyText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val netAmount = qty * rate
    val vatAmount = netAmount * item.taxPercentage / 100.0
    val grossAmount = netAmount + vatAmount
    val canSave = qtyText.toDoubleOrNull() != null && rateText.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${item.itemCode} - ${item.itemName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                AmountRow("VAT", vatAmount.asAmount())
                AmountRow("Gross", grossAmount.asAmount())
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(qty, rate) },
                enabled = canSave
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
