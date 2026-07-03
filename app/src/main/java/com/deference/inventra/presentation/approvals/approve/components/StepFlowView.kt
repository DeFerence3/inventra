package com.deference.inventra.presentation.approvals.approve.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deference.inventra.domain.model.approvals.Step

@Composable
fun StepFlowView(
    steps: List<Step>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Approval Flow",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        steps.sortedBy { it.stepOrder }.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    StepIcon(status = step.status)
                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = if (index < steps.lastIndex) 16.dp else 0.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = step.approverName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = step.designationName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (step.actionAt != null) {
                        Text(
                            text = "${step.status} at ${step.actionAt}",
                            style = MaterialTheme.typography.labelSmall,
                            color = getStatusColor(step.status)
                        )
                    }
                    if (!step.remarks.isNullOrBlank()) {
                        Text(
                            text = "Remarks: ${step.remarks}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIcon(status: String) {
    val (icon, color) = when (status) {
        "Approved" -> Icons.Default.Check to Color(0xFF4CAF50)
        "Rejected" -> Icons.Default.Close to MaterialTheme.colorScheme.error
        else -> Icons.Default.Pending to Color(0xFFFFA000)
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status,
            modifier = Modifier.size(16.dp),
            tint = color
        )
    }
}

private fun getStatusColor(status: String): Color {
    return when (status) {
        "Approved" -> Color(0xFF4CAF50)
        "Rejected" -> Color(0xFFD32F2F)
        else -> Color(0xFFFFA000)
    }
}
