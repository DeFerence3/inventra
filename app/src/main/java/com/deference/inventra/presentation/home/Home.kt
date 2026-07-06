package com.deference.inventra.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deference.inventra.R
import com.deference.inventra.core.Session
import com.deference.inventra.presentation.core.navigation.InventraRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onMenuClick: (InventraRoutes) -> Unit
) {

    val menuItems = listOf(
        MenuItem("GRN", painterResource(R.drawable.ic_grn), InventraRoutes.SupplierList),
        MenuItem("Approvals", painterResource(R.drawable.ic_grn), InventraRoutes.ApprovalList),
        MenuItem("Settings", painterResource(R.drawable.ic_settings)),
    )

    val blue = Color(0xFF1976D2)

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
                        Session.logout()
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

