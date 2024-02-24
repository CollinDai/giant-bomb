package peike.rkt.home

import android.util.Log
import javax.inject.Inject

class SearchRepository @Inject constructor(
  private val searchService: SearchService
) {
  suspend fun search(query: String): SearchResponses {
    val result = searchService.searchGames(query)
    return if (result.isSuccessful) {
      result.body() ?: throw Exception("No results")
    } else {
      Log.e("SearchRepository", "Error: ${result.errorBody()?.string()}")
      throw Exception("Error")
    }
  }
}