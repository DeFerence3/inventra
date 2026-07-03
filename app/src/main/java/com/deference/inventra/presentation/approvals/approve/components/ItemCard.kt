package com.deference.inventra.presentation.approvals.approve.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.purchase.Item

@Composable
fun ItemCard(item: Item) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = androidx.compose.ui.graphics.RectangleShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${item.itemCode} - ${item.itemName}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Qty", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "${item.requiredQty} ${item.unitName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                VerticalDivider()
                Column {
                    Text(text = "Price", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = item.pricePerUnit.asAmount(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                VerticalDivider()
                Column {
                    Text(
                        text = "Gross",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.grossAmount.asAmount(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
