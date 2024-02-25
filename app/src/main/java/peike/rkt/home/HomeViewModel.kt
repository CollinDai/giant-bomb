package peike.rkt.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import peike.rkt.home.SearchUiState.SearchBarUiState
import peike.rkt.home.SearchUiState.SearchResultItem
import peike.rkt.home.SearchUiState.SearchResultUiState
import peike.rkt.home.SearchUiState.SearchResultUiState.Content
import peike.rkt.home.SearchUiState.SearchResultUiState.Initial
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(initialContent())
  val uiState: StateFlow<SearchUiState> by lazy {
    _uiState.apply {
      val searchHistory = searchRepository.getSearchHistory().toList()
      val searchBarUiState = SearchBarUiState(searchHistory)
      update {
        it.copy(searchBarUiState = searchBarUiState)
      }
    }
  }

  fun search(query: String) {
    viewModelScope.launch {
      try {
        _uiState.update { it.copy(searchResultUiState = SearchResultUiState.Loading) }
        val trimmedQuery = query.trim()
        Log.d("HomeViewModel", "Query length: ${trimmedQuery.length}")
        val result = searchRepository.search(trimmedQuery)
        Log.d("HomeViewModel", "Result: $result")
        val items = result.results.map {
          SearchResultItem(
            guid = it.guid,
            name = it.name,
            imageUrl = it.image.super_url,
            thumbnailImageUrl = it.image.thumb_url,
            description = it.deck.orEmpty(),
            releaseDate = it.original_release_date
          )
        }.sortedBy { it.releaseDate }.asReversed()
        val updatedSearchHistory = searchRepository.setSearchHistory(query).toList()
        _uiState.update {
          it.copy(
            searchBarUiState = it.searchBarUiState.copy(recentSearches = updatedSearchHistory),
            searchResultUiState = Content(items)
          )
        }
      } catch (e: Exception) {
        Log.e("HomeViewModel", "Error: ${e.message}", e)
        _uiState.update { it.copy(searchResultUiState = SearchResultUiState.Error) }
      }
    }
  }

  private fun initialContent() = SearchUiState(
    searchBarUiState = SearchBarUiState(emptyList()),
    searchResultUiState = Initial
  )
}