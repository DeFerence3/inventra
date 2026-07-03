package com.deference.inventra.presentation.approvals.list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(status: String) {
    val backgroundColor = when (status) {
        "Pending" -> Color(0xFFFFF3E0)
        "Approved" -> Color(0xFFE8F5E9)
        "Rejected" -> Color(0xFFFFEBEE)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (status) {
        "Pending" -> Color(0xFFE65100)
        "Approved" -> Color(0xFF2E7D32)
        "Rejected" -> Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}
