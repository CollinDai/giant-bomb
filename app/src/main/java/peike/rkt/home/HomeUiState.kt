package peike.rkt.home

sealed interface HomeUiState {
  val searchHistory: List<String>

  data class Loading(override val searchHistory: List<String>) : HomeUiState
  data class Content(
    override val searchHistory: List<String>,
    val results: List<SearchResultItem>
  ) : HomeUiState

  data class Error(
    override val searchHistory: List<String>,
    val error: String
  ) : HomeUiState

  data class SearchResultItem(
    val guid: String,
    val name: String,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val description: String,
  )
}