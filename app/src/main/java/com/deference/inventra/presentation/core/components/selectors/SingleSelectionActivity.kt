package com.deference.inventra.presentation.core.components.selectors

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.presentation.core.components.list.ListItem
import com.deference.inventra.presentation.core.components.list.ListOf
import com.deference.inventra.presentation.core.components.selectors.components.SearchBar
import com.deference.inventra.presentation.core.components.selectors.components.SelectionConstant
import com.deference.inventra.presentation.core.theme.InventraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleSelectionActivity : ComponentActivity() {

    private var appBarTitle = ""
    private var type = ""
    private var immediateShowList = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        enableEdgeToEdge()
        setContent {
            InventraTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SelectionScreen()
                }
            }
        }
    }

    private fun getIntentData() {
        appBarTitle = intent.getStringExtra("head") ?: ""
        type = intent.getStringExtra("type") ?: ""
        immediateShowList = intent.getBooleanExtra("immediateShowList", true)
    }

    private fun sendResult(item: SelectAnyProvider) {
        val selectedItem = when (type) {
            SelectionConstant.LOCATION -> item.item as? Location
            SelectionConstant.ITEM -> item.item as? SearchItem
            else -> null
        } ?: return

        setResult(RESULT_OK, Intent().putExtra("item", selectedItem))
        finish()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SelectionScreen() {
        val viewModel: SingleSelectionVM = hiltViewModel()
        val state by viewModel.singleSelectionState.collectAsState()

        LaunchedEffect(type) {
            viewModel.setSelectionType(type)
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = appBarTitle,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                        .padding(12.dp)
                ) {
                    SearchBar(
                        hint = "Search",
                        immediateSearch = immediateShowList,
                        onTextChange = { query -> viewModel.onSearch(query) },
                        keyboardType = KeyboardType.Text
                    )

                    when {
                        state.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.error != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(state.error ?: "Error", style = MaterialTheme.typography.bodyMedium,color = MaterialTheme.colorScheme.error)
                            }
                        }

                        state.list.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Empty Data")
                            }
                        }

                        else -> {
                            ListOf(
                                items = state.list,
                                listItem = {
                                    ListItem(
                                        modifier = Modifier.clickable{ sendResult(it) },
                                        header = it.title,
//                                        image = it.image,
                                        supportingText = it.subTitle,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
