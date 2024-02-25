package peike.rkt.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import peike.rkt.home.HomeUiState.Content
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow<HomeUiState>(initialContent())
  val uiState: StateFlow<HomeUiState> by lazy {
    _uiState.apply {
      val searchHistory = searchRepository.getSearchHistory().toList()
      update {
        (it as? Content)?.copy(searchHistory = searchHistory) ?: it
      }
    }
  }

  fun search(query: String) {
    viewModelScope.launch {
      try {
        _uiState.update { HomeUiState.Loading(it.searchHistory) }
        val result = searchRepository.search(query)
        Log.d("HomeViewModel", "Result: $result")
        val items = result.results.map {
          HomeUiState.SearchResultItem(
            guid = it.guid,
            name = it.name,
            imageUrl = it.image.super_url,
            thumbnailImageUrl = it.image.thumb_url,
            description = it.deck
          )
        }
        val updatedSearchHistory = searchRepository.setSearchHistory(query).toList()
        _uiState.value = Content(updatedSearchHistory, items)
      } catch (e: Exception) {
        Log.e("HomeViewModel", "Error: ${e.message}", e)
        _uiState.update { HomeUiState.Error(it.searchHistory, "Fail to get search result") }
      }
    }
  }

  private fun initialContent() = Content(emptyList(), emptyList())
}