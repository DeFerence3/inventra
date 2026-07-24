package com.deference.inventra.presentation.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.deference.inventra.presentation.approvals.approve.ApproveScreen
import com.deference.inventra.presentation.approvals.approve.ApproveVM
import com.deference.inventra.presentation.approvals.list.ApprovalsListScreen
import com.deference.inventra.presentation.approvals.list.ApprovalsListVM
import com.deference.inventra.presentation.core.ocr.CameraScreen
import com.deference.inventra.presentation.home.Home
import com.deference.inventra.presentation.login.Login
import com.deference.inventra.presentation.login.LoginVM
import com.deference.inventra.presentation.orderItem.OrderItemListScreen
import com.deference.inventra.presentation.orderItem.OrderItemListVM
import com.deference.inventra.presentation.purchaseOrder.PurchaseOrderListScreen
import com.deference.inventra.presentation.purchaseOrder.PurchaseOrderVM
import com.deference.inventra.presentation.purchaserequest.PurchaseRequestScreen
import com.deference.inventra.presentation.purchaserequest.PurchaseRequestVM
import com.deference.inventra.presentation.spotcheck.SpotCheckScreen
import com.deference.inventra.presentation.spotcheck.SpotCheckVM
import com.deference.inventra.presentation.stockreceipt.StockReceiptDetailScreen
import com.deference.inventra.presentation.stockreceipt.StockReceiptDetailVM
import com.deference.inventra.presentation.stockreceipt.StockReceiptListScreen
import com.deference.inventra.presentation.stockreceipt.StockReceiptListVM
import com.deference.inventra.presentation.stockrequest.StockRequestScreen
import com.deference.inventra.presentation.stockrequest.StockRequestVM
import com.deference.inventra.presentation.supplier.SupplierListScreen
import com.deference.inventra.presentation.supplier.SupplierVM
import kotlinx.serialization.json.Json

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean
) {
    val backStack = rememberNavBackStack(if (isLoggedIn) InventraRoutes.Home else InventraRoutes.Login)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(checkNotNull(LocalViewModelStoreOwner.current) {
                ViewModelStoreOwner(viewModelStore = ViewModelStore())
            })
        ),
        entryProvider = { key ->
            when(key){
                is InventraRoutes -> {
                    when(key){
                        InventraRoutes.Home -> NavEntry(key){
                            Home {
                                backStack.add(it)
                            }
                        }
                        is InventraRoutes.ItemList -> NavEntry(key){
                            val vm = hiltViewModel<OrderItemListVM, OrderItemListVM.Factory>(
                                creationCallback = { factory ->
                                    factory.create(
                                        key.supplierId,
                                        key.supplierName,
                                        key.poUUIDs ?: emptyList(),
                                        key.scannedBillJson
                                    )
                                }
                            )
                            val state by vm.state.collectAsState()
                            OrderItemListScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onSave = {
                                    val index = backStack.indexOf(InventraRoutes.Home)
                                    if (index != -1) {
                                        while (backStack.lastIndex > index) {
                                            backStack.removeLastOrNull()
                                        }
                                    }
                                },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        InventraRoutes.Login -> NavEntry(key){
                            val vm = hiltViewModel<LoginVM>()
                            val state by vm.state.collectAsState()
                            Login(
                                onLoginSuccess = {
                                    backStack.add(InventraRoutes.Home)
                                },
                                state = state,
                                onAction = vm::onAction,
                                event = vm.eventFlow,
                                modifier = Modifier
                            )
                        }
                        is InventraRoutes.PurchaseOrderList -> NavEntry(key){
                            val vm = hiltViewModel<PurchaseOrderVM, PurchaseOrderVM.Factory>(
                                creationCallback = { factory ->
                                    factory.create(key.supplierId)
                                }
                            )
                            val state by vm.state.collectAsState()
                            PurchaseOrderListScreen(
                                onContinue = { poUUIDs ->
                                    backStack.add(
                                        InventraRoutes.ItemList(
                                            poUUIDs,
                                            key.supplierId,
                                            key.supplierName
                                        )
                                    )
                                },
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction,
                            )
                        }
                        InventraRoutes.SupplierList -> NavEntry(key){
                            val vm = hiltViewModel<SupplierVM>()
                            val state by vm.state.collectAsState()
                            SupplierListScreen(
                                onSupplierSelected = { supplier ->
                                    backStack.add(InventraRoutes.PurchaseOrderList(supplier.id,supplier.name))
                                },
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        InventraRoutes.ApprovalList -> NavEntry(key){
                            val vm = hiltViewModel<ApprovalsListVM>()
                            val state by vm.state.collectAsState()
                            ApprovalsListScreen(
                                onBack = { backStack.removeLastOrNull() },
                                onApprovalClick = { type,id,transUuId ->
                                    backStack.add(InventraRoutes.Approve(type,id,transUuId.split(",")))
                                },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }

                        is InventraRoutes.Approve -> NavEntry(key){
                            val vm = hiltViewModel<ApproveVM, ApproveVM.Factory>(
                                creationCallback = { factory ->
                                    factory.create(key.type,key.approvalId,key.transUuId)
                                }
                            )
                            val state by vm.state.collectAsState()
                            ApproveScreen(
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        InventraRoutes.PurchaseRequest -> NavEntry(key){
                            val vm = hiltViewModel<PurchaseRequestVM>()
                            val state by vm.state.collectAsState()
                            PurchaseRequestScreen(
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        InventraRoutes.SpotCheck -> NavEntry(key){
                            val vm = hiltViewModel<SpotCheckVM>()
                            val state by vm.state.collectAsState()
                            SpotCheckScreen(
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }

                        InventraRoutes.Scan -> NavEntry(key){
                            CameraScreen(
                                onBillScanned = { scannedBill ->
                                    val json = Json.encodeToString(
                                        com.deference.inventra.domain.model.ocr.ScannedBill.serializer(),
                                        scannedBill
                                    )
                                    backStack.add(
                                        InventraRoutes.ItemList(
                                            poUUIDs = emptyList(),
                                            supplierId = 1,
                                            supplierName = scannedBill.supplierName,
                                            scannedBillJson = json
                                        )
                                    )
                                },
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        InventraRoutes.StockRequest -> NavEntry(key){
                            val vm = hiltViewModel<StockRequestVM>()
                            val state by vm.state.collectAsState()
                            StockRequestScreen(
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        InventraRoutes.StockReceiptList -> NavEntry(key){
                            val vm = hiltViewModel<StockReceiptListVM>()
                            val state by vm.state.collectAsState()
                            StockReceiptListScreen(
                                onBack = { backStack.removeLastOrNull() },
                                onReceiptClick = { receipt ->
                                    backStack.add(InventraRoutes.StockReceiptDetails(receipt.uuid))
                                },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAction = vm::onAction
                            )
                        }
                        is InventraRoutes.StockReceiptDetails -> NavEntry(key){
                            val vm = hiltViewModel<StockReceiptDetailVM,StockReceiptDetailVM.Factory>(
                                creationCallback = { factory ->
                                    factory.create(key.transNo)
                                }
                            )
                            val state by vm.state.collectAsState()
                            StockReceiptDetailScreen(
                                onBack = { backStack.removeLastOrNull() },
                                state = state,
                                eventFlow = vm.eventFlow,
                                onAccept = { vm.acceptReceipt() },
                                onDecline = { vm.declineReceipt() }
                            )
                        }
                    }


                }
                else -> error("No route for $key")
            }
        }
    )
}

