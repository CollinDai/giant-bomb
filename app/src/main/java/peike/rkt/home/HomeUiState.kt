package peike.rkt.home

data class SearchUiState(
  val searchBarUiState: SearchBarUiState,
  val searchResultUiState: SearchResultUiState
) {
  data class SearchBarUiState(
    val recentSearches: List<String>
  )

  sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object Initial : SearchResultUiState
    data object Error : SearchResultUiState
    data class Content(val results: List<SearchResultItem>) : SearchResultUiState
  }

  sealed interface FastSearchUiState {
    data object Loading : FastSearchUiState
    data class Content(val results: List<SearchResultItem>) : FastSearchUiState
  }

  data class SearchResultItem(
    val guid: String,
    val name: String,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val description: String,
    val releaseDate: String?,
  )
}