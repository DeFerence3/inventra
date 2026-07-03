package com.deference.inventra.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.deference.inventra.core.Session
import com.deference.inventra.presentation.core.navigation.NavigationRoot
import com.deference.inventra.presentation.core.theme.InventraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = {
                    Session.isAppInitializing
                }
            )
        }
        setContent {
            InventraTheme {
//                InventraApp()
                NavigationRoot(isLoggedIn = Session.isLoggedIn)
                /*val vm = hiltViewModel<OrderItemListVM, OrderItemListVM.Factory>(
                    creationCallback = { factory ->
                        factory.create(2,"Acme Supplie",listOf("a4f84d07-74ee-473a-bd48-cdfa8b6ca4f6"))
                    }
                )
                val state by vm.state.collectAsState()
                Log.i("Inventra", "LoggedIn---> ${Session.isLoggedIn}")
                OrderItemListScreen(
                    onBack = {
                        //backS tack.removeLastOrNull()
                    },
                    state = state,
                    eventFlow = vm.eventFlow,
                    onAction = vm::onAction
                )*/
            }
        }
    }
}

/*
@Composable
fun InventraApp() {
    var backStack by remember {
        val initialRoute = if (Session.isLoggedIn) InventraRoutes.Home else InventraRoutes.Login
        mutableStateOf(listOf(initialRoute))
    }

    when (val currentRoute = backStack.last()) {
        is InventraRoutes.Login -> {
            val vm = hiltViewModel<LoginVM>()
            val state by vm.state.collectAsState()
            Login(
                onLoginSuccess = {
                    backStack = listOf(InventraRoutes.Home)
                },
                state = state,
                onAction = vm::onAction,
                event = vm.eventFlow,
                modifier = Modifier
            )
        }

        is InventraRoutes.Home -> {
            Home(onMenuClick = { newRoute ->
                backStack = backStack + newRoute
            })
        }

        is InventraRoutes.SupplierList -> {
            val vm = hiltViewModel<SupplierVM>()
            val state by vm.state.collectAsState()
            SupplierListScreen(
                onSupplierSelected = { supplier ->
                    backStack = backStack + InventraRoutes.PurchaseOrderList(supplier.id)
                },
                onBack = { backStack = backStack.dropLast(1) },
                state = state,
                eventFlow = vm.eventFlow,
                onAction = vm::onAction
            )
        }

        is InventraRoutes.PurchaseOrderList -> {
            val vm = hiltViewModel<PurchaseOrderVM>()
            val state by vm.state.collectAsState()
            PurchaseOrderListScreen(
                onContinue = { poUUIDs ->
                    backStack = backStack + InventraRoutes.ItemList(poUUIDs)
                },
                onBack = { backStack = backStack.dropLast(1) },
                viewModel = vm
            )
        }

        is InventraRoutes.ItemList -> {
            val vm = hiltViewModel<OrderItemListVM>()
            val state by vm.state.collectAsState()
            OrderItemListScreen(
                poUUIDs = currentRoute.poUUIDs,
                onBack = { backStack = backStack.dropLast(1) },
                state = state,
                eventFlow = vm.eventFlow,
                onAction = vm::onAction
            )
        }
    }
}*/
