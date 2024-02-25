package peike.rkt.home

import android.util.Log
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import peike.rkt.home.SearchUiState.SearchResultUiState
import peike.rkt.home.SearchUiState.SearchResultUiState.Content
import peike.rkt.home.SearchUiState.SearchResultUiState.Initial
import peike.rkt.home.SearchUiState.SearchResultUiState.Loading

class HomeViewModelTest {
  @MockK
  lateinit var searchRepository: SearchRepository

  @InjectMockKs
  lateinit var subject: HomeViewModel

  init {
    MockKAnnotations.init(this)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  companion object {
    @JvmStatic
    @BeforeClass
    fun setup() {
      Dispatchers.setMain(StandardTestDispatcher())
      mockkStatic(Log::class)
      every { Log.v(any(), any()) } returns 0
      every { Log.d(any(), any()) } returns 0
      every { Log.i(any(), any()) } returns 0
      every { Log.e(any(), any(), any()) } returns 0
    }

    @JvmStatic
    @AfterClass
    fun teardown() {
      Dispatchers.resetMain()
      unmockkStatic(Log::class)
    }
  }

  @Test
  fun `should return initial state with empty search history first`() = runTest {

    val searchHistory = emptySet<String>()
    coEvery { searchRepository.getSearchHistory() } returns searchHistory

    subject.uiState.test {
      val item = awaitItem()
      assertThat(item.searchBarUiState.recentSearches).isEmpty()
      assertThat(item.searchResultUiState).isEqualTo(Initial)
      expectNoEvents()
    }
  }

  @Test
  fun `should emit empty list if no search result found`() = runTest {
    val query = "query"
    coEvery { searchRepository.search(query) } returns mockk {
      every { results } returns emptyList()
    }
    coEvery { searchRepository.setSearchHistory(any()) } returns emptySet()
    coEvery { searchRepository.getSearchHistory() } returns emptySet()


    subject.uiState.test {
      assertThat(awaitItem().searchResultUiState).isEqualTo(Initial)

      subject.search(query)
      val item = awaitItem()
      assertThat(item.searchResultUiState).isEqualTo(Loading)

      val item2 = awaitItem()
      assertThat(item2.searchResultUiState).isEqualTo(Content(emptyList()))
      expectNoEvents()
    }
  }

  @Test
  fun `should emit error state if exception happens during search`() = runTest {
    val query = "query"
    coEvery { searchRepository.search(query) } throws Exception()
    coEvery { searchRepository.getSearchHistory() } returns emptySet()

    subject.uiState.test {
      assertThat(awaitItem().searchResultUiState).isEqualTo(Initial)

      subject.search(query)
      val item = awaitItem()
      assertThat(item.searchResultUiState).isEqualTo(Loading)

      val item2 = awaitItem()
      assertThat(item2.searchResultUiState).isEqualTo(SearchResultUiState.Error)
      expectNoEvents()
    }
  }

  @Test
  fun `should emit content`() = runTest {
    val query = "query"

    coEvery { searchRepository.search(query) } returns getFakeResponse()
    coEvery { searchRepository.setSearchHistory(any()) } returns emptySet()
    coEvery { searchRepository.getSearchHistory() } returns emptySet()

    subject.uiState.test {
      assertThat(awaitItem().searchResultUiState).isEqualTo(Initial)

      subject.search(query)
      val item = awaitItem()
      assertThat(item.searchResultUiState).isEqualTo(Loading)

      val item2 = awaitItem()
      assertThat(item2.searchResultUiState).isInstanceOf(Content::class.java)
      val content = item2.searchResultUiState as Content
      assertThat(content.results).hasSize(1)
      assertThat(content.results.firstOrNull() {
        it.name == "name" &&
          it.thumbnailImageUrl == "thumb_url" &&
          it.imageUrl == "super_url" &&
          it.description == "deck"
      }).isNotNull()
      expectNoEvents()
    }

    coVerify { searchRepository.setSearchHistory(query) }
  }

  private fun getFakeResponse(): SearchResponses {

    val searchResult = listOf(
      Game(
        aliases = null,
        api_detail_url = "api_detail_url",
        date_added = "date_added",
        date_last_updated = "date_last_updated",
        deck = "deck",
        description = "description",
        expected_release_day = null,
        expected_release_month = null,
        expected_release_quarter = null,
        expected_release_year = null,
        guid = "guid",
        id = 1,
        image = Image(
          icon_url = "icon_url",
          medium_url = "medium_url",
          screen_url = "screen_url",
          screen_large_url = "screen_large_url",
          small_url = "small_url",
          super_url = "super_url",
          thumb_url = "thumb_url",
          tiny_url = "tiny_url",
          original_url = "original_url",
          image_tags = "image_tags"
        ),
        image_tags = emptyList(),
        name = "name",
        number_of_user_reviews = 1,
        original_game_rating = emptyList(),
        original_release_date = "original_release_date",
        platforms = emptyList(),
        site_detail_url = "site_detail_url"
      )
    )

    return SearchResponses(
      error = "error",
      limit = 1,
      offset = 1,
      number_of_page_results = 1,
      number_of_total_results = 1,
      status_code = 1,
      results = searchResult
    )
  }
}