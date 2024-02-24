package peike.rkt.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Content(emptyList()))
  val uiState: StateFlow<HomeUiState> = _uiState

  fun search(query: String) {
    viewModelScope.launch {
      try {
        _uiState.value = HomeUiState.Loading
        val result = searchRepository.search(query)
        Log.d("HomeViewModel", "Result: $result")
        val items = result.results.map {
          HomeUiState.SearchResultItem(
            guid = it.guid,
            name = it.name
          )
        }
        _uiState.value = HomeUiState.Content(items)
      } catch (e: Exception) {
        Log.e("HomeViewModel", "Error: ${e.message}", e)
        _uiState.value = HomeUiState.Error("Error")
      }
    }
  }
}