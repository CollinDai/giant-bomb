package peike.rkt.home

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import javax.inject.Inject

class SearchRepository @Inject constructor(
  private val searchService: SearchService,
  private val sharedPreferences: SharedPreferences
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

  fun getSearchHistory(): Set<String> {
    return sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, emptySet()).orEmpty()
  }

  fun setSearchHistory(query: String): Set<String> {
    val history = getSearchHistory().toMutableSet()
    history.add(query.trim())
    sharedPreferences.edit {
      this.putStringSet(SEARCH_HISTORY_KEY, history)
    }

    return history
  }

  companion object {
    private const val SEARCH_HISTORY_KEY = "search_history"
  }
}