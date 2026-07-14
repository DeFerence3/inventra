package com.deference.inventra.presentation.core.ocr

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.deference.inventra.core.utils.asAmount
import com.deference.inventra.domain.model.ocr.ScannedBill
import com.deference.inventra.presentation.core.components.AppButton
import com.deference.inventra.presentation.core.components.AppButtonType
import com.deference.inventra.presentation.core.components.PermissionRequestDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onBillScanned: (ScannedBill) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture by produceState<ListenableFuture<ProcessCameraProvider>?>(initialValue = null) {
        value = ProcessCameraProvider.getInstance(context)
    }
    var showOpenSettingsWhenPermissionPermDenied by remember { mutableStateOf(false) }

    val cameraPermission = rememberPermissionState( Manifest.permission.CAMERA){
        if (!it) showOpenSettingsWhenPermissionPermDenied = true
    }
    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }
    if (showOpenSettingsWhenPermissionPermDenied){
        PermissionRequestDialog(
            onDenied = { showOpenSettingsWhenPermissionPermDenied = false },
            onGrand = {
                showOpenSettingsWhenPermissionPermDenied = false
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent,null)
            },
            permissions = "Camera"
        )
    }

    var scannedBill by remember { mutableStateOf<ScannedBill?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = modifier,
            factory = { androidViewContext ->
                PreviewView(androidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            }
        ) { previewView ->
            setUpCamera(
                previewView = previewView,
                lifecycleOwner = lifecycleOwner,
                cameraProviderFuture = cameraProviderFuture,
                context = context
            ) { text ->
                if (scannedBill == null && text != null && BillParser.isBill(text)) {
                    scannedBill = BillParser.parse(text)
                }
            }
        }

        // Back button on top of camera preview
        if (scannedBill == null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        scannedBill?.let { bill ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bill Detected!",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        // Supplier Info
                        Text(
                            text = "Supplier: ${bill.supplierName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Inv No: ${bill.invoiceNo}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Date: ${bill.date}", style = MaterialTheme.typography.bodyMedium)
                        }

                        // Items list
                        Text(
                            text = "Items Detected:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        
                        bill.items.forEach { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = item.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Qty: ${item.qty}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Rate: ${item.rate.asAmount()}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Tax: ${item.tax.asAmount()}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Gross: ${item.gross.asAmount()}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        // Total Amount
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = bill.totalAmount.asAmount(),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )
                        }

                        // Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppButton(
                                modifier = Modifier.weight(1f),
                                text = "Scan Again",
                                type = AppButtonType.Outlined,
                                onClick = { scannedBill = null }
                            )
                            AppButton(
                                modifier = Modifier.weight(1f),
                                text = "GRN",
                                onClick = { onBillScanned(bill) }
                            )
                        }
                    }
                }
            }
        }
    }
}