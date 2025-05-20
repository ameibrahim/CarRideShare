package com.lubna.carrideshare
import retrofit2.HttpException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import android.util.Log

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


class SearchRidesRepository(private val api: ApiService) {

    suspend fun searchRides(query: String): List<RideResponse> {
        val searchRequest = SearchRequest(query)
        val resp = api.searchRides(searchRequest)
        if (resp.isSuccessful) {
            return resp.body()?.results ?: emptyList()
        }
        throw HttpException(resp)
    }

}

class SearchRidesViewModel(
    private val repo: SearchRidesRepository
) : ViewModel() {

    private val _allRides = MutableStateFlow<List<RideResponse>>(emptyList())
    val filteredRides: StateFlow<List<RideResponse>> = _allRides.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    try {
                        val results = if (query.isBlank()) {
                            emptyList()
                        } else {
                            repo.searchRides(query)
                        }
                        Log.d("SearchRidesViewModel", "Search query: $query, results count: ${results.size}")
                        _allRides.value = results
                    } catch (e: Exception) {
                        Log.e("SearchRidesViewModel", "Error searching rides", e)
                        _allRides.value = emptyList()
                    }
                }
        }
    }

    fun onSearchQueryChange(q: String) {
        _searchQuery.value = q
    }
}