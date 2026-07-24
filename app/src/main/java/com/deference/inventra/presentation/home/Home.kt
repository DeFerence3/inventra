package com.deference.inventra.presentation.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deference.inventra.R
import com.deference.inventra.core.Session
import com.deference.inventra.presentation.core.components.PermissionRequestDialog
import com.deference.inventra.presentation.core.navigation.InventraRoutes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Home(
    onMenuClick: (InventraRoutes) -> Unit
) {

    val menuItems = listOf(
        MenuItem("GRN", painterResource(R.drawable.ic_grn), InventraRoutes.SupplierList),
        MenuItem("Purchase Request", painterResource(R.drawable.ic_cart), InventraRoutes.PurchaseRequest),
        MenuItem("Stock Request", painterResource(R.drawable.ic_request), InventraRoutes.StockRequest),
        MenuItem("Stock Receipt", painterResource(R.drawable.ic_receive), InventraRoutes.StockReceiptList),
        MenuItem("Approvals", painterResource(R.drawable.ic_approval), InventraRoutes.ApprovalList),
        MenuItem("Spot Check", painterResource(R.drawable.ic_stock), InventraRoutes.SpotCheck),
        MenuItem("Scan", rememberVectorPainter(image = Icons.Default.DocumentScanner), InventraRoutes.Scan),
        MenuItem("Settings", painterResource(R.drawable.ic_settings)),
    )

    val blue = Color(0xFF1976D2)
    val context = LocalContext.current
    var isLogoutDialogShowing by remember { mutableStateOf(false) }
    var showOpenSettingsWhenPermissionDenied by remember { mutableStateOf(false) }

    val notificationPermission = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS){
        if (!it) showOpenSettingsWhenPermissionDenied = true
    }

    LaunchedEffect(Unit) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }
    }

    if (showOpenSettingsWhenPermissionDenied){
        PermissionRequestDialog(
            onDenied = { showOpenSettingsWhenPermissionDenied = false },
            onGrand = {
                showOpenSettingsWhenPermissionDenied = false
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent,null)
            },
            permissions = "Notification"
        )
    }

    if (isLogoutDialogShowing){
        LogoutDialog(
            modifier = Modifier,
            onDismiss = {
                isLogoutDialogShowing = isLogoutDialogShowing.not()
            },
            onLogout = {
                Session.logout()
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Inventra",
                        color = blue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                actions = { IconButton(
                    onClick = {
                        isLogoutDialogShowing = isLogoutDialogShowing.not()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = blue,
                        modifier = Modifier.size(30.dp)
                    )
                } },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = blue,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(30.dp)
                    )
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuItems) { item ->
                MenuCard(
                    item = item
                ) {
                    item.route?.let {
                        onMenuClick(it)
                    }
                }
            }
        }
    }
}

