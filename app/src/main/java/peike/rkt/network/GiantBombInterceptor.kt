package peike.rkt.network

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import peike.rkt.BuildConfig
import peike.rkt.home.SearchService.Companion.QUERY_FILTER

class GiantBombInterceptor : Interceptor {
  override fun intercept(chain: Chain): Response {
    val apiKey = BuildConfig.GIANT_BOMB_API_KEY
    val originalRequest = chain.request()
    val queryValue = originalRequest.url.queryParameter(QUERY_FILTER)
    val newUrl = originalRequest.url.newBuilder()
      .addQueryParameter("format", "json")
      .addQueryParameter("api_key", apiKey)
      .addQueryParameter("sort", "original_release_date:desc")
      .setQueryParameter(QUERY_FILTER, "name:$queryValue")
      .build()
    val newRequest = originalRequest.newBuilder().url(newUrl).build()
    return chain.proceed(newRequest)
  }
}