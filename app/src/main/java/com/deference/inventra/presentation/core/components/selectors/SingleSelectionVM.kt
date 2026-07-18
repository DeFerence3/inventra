package com.deference.inventra.presentation.core.components.selectors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.data.remote.ItemApiService
import com.deference.inventra.domain.usecase.GetLocationsUseCase
import com.deference.inventra.presentation.core.components.selectors.components.SelectionConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import javax.inject.Inject

@HiltViewModel
class SingleSelectionVM @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val itemApiService: ItemApiService
) : ViewModel() {

    private val _singleSelectionState = MutableStateFlow(SingleSelectionState())
    val singleSelectionState: StateFlow<SingleSelectionState> = _singleSelectionState.asStateFlow()

    private var searchJob: Job? = null

    fun setSelectionType(type: String) {
        if (_singleSelectionState.value.type == type && _singleSelectionState.value.list.isNotEmpty()) return
        updateState { it.copy(type = type) }
        onSearch("")
    }

    fun onSearch(query: String) {
        updateState { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            val list = when (_singleSelectionState.value.type) {
                SelectionConstant.LOCATION -> loadLocations(query)
                SelectionConstant.ITEM -> loadItems(query)
                else -> emptyList()
            }
            updateState { it.copy(list = list, isLoading = false) }
        }
    }

    private suspend fun loadLocations(query: String): List<SelectAnyProvider> {
        var list = emptyList<SelectAnyProvider>()
        getLocationsUseCase(query.ifEmpty { null }, 1, 50).collectLatest { result ->
            when(result){
                is RequestState.Error -> updateState { it.copy(error = result.message) }
                RequestState.Loading -> Unit
                is RequestState.Success -> {
                    list = result.data.map { location ->
                        SelectAnyProvider(
                            title = location.name,
                            subTitle = location.code,
                            item = location
                        )
                    }
                }
            }
        }
        return list
    }

    private suspend fun loadItems(query: String): List<SelectAnyProvider> {
        return try {
            val response = itemApiService.getItemList(query, 1, 50).await()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.items.map { item ->
                    SelectAnyProvider(
                        title = item.itemName,
                        subTitle = item.itemCode?.let { "Code: $it" },
                        item = item
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
            updateState { it.copy(error = "Invalid response from server") }
            emptyList()
        }catch (e: Exception) {
            e.printStackTrace()
            updateState { it.copy(error = "Unhandled Exception: ${e.message}") }
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private inline fun updateState(update: (SingleSelectionState) -> SingleSelectionState) {
        _singleSelectionState.value = update(_singleSelectionState.value)
    }
}
