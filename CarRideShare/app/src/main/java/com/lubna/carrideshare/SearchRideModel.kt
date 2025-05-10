package com.lubna.carrideshare
import retrofit2.HttpException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class SearchRidesViewModelFactory(
    private val repo: SearchRidesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRidesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRidesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

// --- Repository stays the same ---
class SearchRidesRepository(private val api: ApiService) {
    suspend fun fetchRides(): List<RideResponse> {
        val resp = api.getRides()
        if (resp.isSuccessful) {
            return resp.body() ?: emptyList()
        }
        throw HttpException(resp)
    }
}

// --- ViewModel with StateFlow-based filtering ---
class SearchRidesViewModel(
    private val repo: SearchRidesRepository
) : ViewModel() {

    // 1) All rides from the network
    private val _allRides = MutableStateFlow<List<RideResponse>>(emptyList())
    val allRides: StateFlow<List<RideResponse>> = _allRides.asStateFlow()

    // 2) Current search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 3) Combined & filtered list
    val filteredRides: StateFlow<List<RideResponse>> =
        combine(_allRides, _searchQuery) { rides, query ->
            if (query.isBlank()) rides
            else rides.filter { it.id.contains(query, ignoreCase = true) }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    init {
        viewModelScope.launch {
            try {
                _allRides.value = repo.fetchRides()
            } catch (e: Exception) {
                // TODO: handle error
            }
        }
    }

    // Called from your TextField’s onValueChange
    fun onSearchQueryChange(q: String) {
        _searchQuery.value = q
    }
}
