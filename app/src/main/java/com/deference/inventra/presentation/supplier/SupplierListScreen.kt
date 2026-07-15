package com.deference.inventra.presentation.supplier

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deference.inventra.domain.model.master.Supplier
import com.deference.inventra.presentation.core.components.InputTextField
import com.deference.inventra.presentation.core.components.InputTextFieldType
import com.deference.inventra.presentation.core.components.list.ListItem
import com.deference.inventra.presentation.core.components.list.ListOf
import com.deference.inventra.presentation.core.utils.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierListScreen(
    onSupplierSelected: (Supplier) -> Unit,
    onBack: () -> Unit,
    state: SupplierState,
    eventFlow: Flow<SupplierEvent>,
    onAction: (SupplierActions) -> Unit
) {
    val context = LocalContext.current

    eventFlow.ObserveEvent { event ->
        when (event) {
            is SupplierEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val listState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= state.suppliers.size - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !state.isLoading && !state.isLastPage) {
            onAction(SupplierActions.LoadNextPage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Supplier") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)
        ) {
            InputTextField(
                text = state.searchQuery,
                onValueChange = { onAction(SupplierActions.OnSearchQueryChanged(it)) },
                label = "Search",
                leadingIcon = Icons.Default.Search,
                type = InputTextFieldType.WithIcon
            )

            Spacer(modifier = Modifier.height(16.dp))

            ListOf(
                items = state.suppliers,
                listItem = {
                    ListItem(
                        header = it.name,
                        supportingText = it.code,
                        modifier = Modifier
                            .clickable(onClick = { onSupplierSelected(it) } )
                    )
                },
                state = listState,
                modifier = Modifier.fillMaxSize(),
                additionalItem = if (state.isLoading) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                } else null
            )
        }
    }
}
