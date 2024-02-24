package peike.rkt.home

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

  @GET("games")
  suspend fun searchGames(
    @Query(QUERY_FILTER) query: String
  ): Response<SearchResponses>

  companion object {
    const val QUERY_FILTER = "filter"
  }
}