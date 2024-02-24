package peike.rkt.home

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data class Content(val results: List<SearchResultItem>) : HomeUiState
  data class Error(val error: String) : HomeUiState

  data class SearchResultItem(
    val guid: String,
    val name: String
  )
}