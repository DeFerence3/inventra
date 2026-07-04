@file:Suppress("DEPRECATION")
package com.deference.inventra.presentation.orderItem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.core.utils.now
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryChallanDialog(
    onDismiss: () -> Unit,
    onConfirm: (deliveryChallanNo: String, deliveryChallanDate: LocalDateTime) -> Unit,
) {
    var challanNo by remember { mutableStateOf("") }
    var challanDate by remember { mutableStateOf(LocalDateTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val canConfirm = challanNo.isNotBlank()

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = challanDate.toInstant(TimeZone.UTC).toEpochMilliseconds()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            @Suppress("DEPRECATION")
                            val selectedDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC)
                            val currentTime = LocalDateTime.now()
                            @Suppress("DEPRECATION")
                            challanDate = LocalDateTime(
                                year = selectedDate.year,
                                monthNumber = selectedDate.monthNumber,
                                dayOfMonth = selectedDate.dayOfMonth,
                                hour = currentTime.hour,
                                minute = currentTime.minute,
                                second = currentTime.second,
                                nanosecond = currentTime.nanosecond
                            )
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delivery Challan Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = challanNo,
                    onValueChange = { challanNo = it },
                    label = { Text("Delivery Challan No") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = challanDate.formatToString(),
                        onValueChange = {},
                        label = { Text("Delivery Challan Date") },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(challanNo, challanDate)
                },
                enabled = canConfirm
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
