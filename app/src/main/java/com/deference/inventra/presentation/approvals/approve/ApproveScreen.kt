package com.deference.inventra.presentation.approvals.approve

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.core.utils.formatToString
import com.deference.inventra.domain.model.purchase.Item
import com.deference.inventra.presentation.approvals.approve.components.DetailRow
import com.deference.inventra.presentation.approvals.approve.components.ItemCard
import com.deference.inventra.presentation.approvals.approve.components.ItemDetailsBottomSheet
import com.deference.inventra.presentation.approvals.approve.components.StepFlowView
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.AppButtonType
import com.deference.inventra.presentation.core.components.ErrorDialog
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.components.InputTextFieldType
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApproveScreen(
    onBack: () -> Unit,
    state: ApproveState,
    eventFlow: Flow<ApproveEvent>,
    onAction: (ApproveAction) -> Unit,
) {
    val context = LocalContext.current
    var clickedItem by remember { mutableStateOf<Item?>(null) }
    eventFlow.ObserveEvent { event ->
        when (event) {
            is ApproveEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            ApproveEvent.Success -> {
                Toast.makeText(context, "Action Performed Successfully", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }

    if (state.error != null) {
        ErrorDialog(error = state.error, onDismiss = { onAction(ApproveAction.OnDismissError) })
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Approve / Reject") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = state.selectedApprovalDetails
            ?.takeIf { it.status == "Pending" }
            ?.let { details ->
                @Composable {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppButton(
                            modifier = Modifier.weight(1f),
                            text = "Reject",
                            onClick = {
                                onAction(ApproveAction.OnReject(details.uuid))
                            },
                            type = AppButtonType.Outlined,
                            enabled = !state.isPerformingAction
                        )
                        AppButton(
                            modifier = Modifier.weight(1f),
                            text = "Approve",
                            onClick = { onAction(ApproveAction.OnApprove(details.uuid)) },
                            enabled = !state.isPerformingAction
                        )
                    }
                }
            } ?: {}
    ) { paddingValues ->

        clickedItem?.let { item ->
            ItemDetailsBottomSheet(
                item = item,
                onDismiss = { clickedItem = null }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoadingDetails) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.selectedApprovalDetails != null) {
                val details = state.selectedApprovalDetails
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    DetailRow("Trans No", details.transNo)
                    DetailRow("Date", details.transDate.formatToString())
                    DetailRow("Amount", (details.amount ?: 0.0).asAmount())
                    DetailRow("Requested By", details.requestedUserName)

                    if (state.items.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Items",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        state.items.forEach { item ->
                            ItemCard(item = item){
                                clickedItem = item
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else if (state.isLoadingItems) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    StepFlowView(
                        steps = details.steps,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Status: ${details.status}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (details.status == "Pending") {
                        InputTextField(
                            text = state.comment,
                            onValueChange = { onAction(ApproveAction.OnCommentChange(it)) },
                            label = "Comment",
                            type = InputTextFieldType.Outlined,
                            maxLine = 3,
                            error = state.commentError
                        )
                    } else {
                        Text(
                            "Action already taken: ${details.status}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Text(
                    text = "No details found",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
