package peike.rkt.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import peike.rkt.R
import peike.rkt.home.SearchUiState.SearchBarUiState
import peike.rkt.home.SearchUiState.SearchResultItem
import peike.rkt.home.SearchUiState.SearchResultUiState
import peike.rkt.home.SearchUiState.SearchResultUiState.Content
import peike.rkt.home.SearchUiState.SearchResultUiState.Error
import peike.rkt.home.SearchUiState.SearchResultUiState.Initial
import peike.rkt.home.SearchUiState.SearchResultUiState.Loading
import peike.rkt.ui.theme.GiantBombTheme

@Composable
fun SearchScreen(
  uiState: SearchUiState,
  modifier: Modifier = Modifier,
  onSearch: (String) -> Unit,
  onSelect: (SearchResultItem) -> Unit
) {
  Column(modifier = modifier.fillMaxSize()) {
    TopSearchBar(
      searchBarUiState = uiState.searchBarUiState,
      onSearch = onSearch,
    )
    SearchResultView(
      uiState = uiState.searchResultUiState,
      modifier = Modifier.padding(horizontal = 16.dp),
      onSelect = onSelect
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchBar(
  searchBarUiState: SearchBarUiState,
  modifier: Modifier = Modifier,
  onSearch: (String) -> Unit
) {
  var text by rememberSaveable { mutableStateOf("") }
  var active by rememberSaveable { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .semantics { isTraversalGroup = true }) {
    SearchBar(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .semantics { traversalIndex = -1f },
      query = text,
      onQueryChange = { text = it },
      onSearch = {
        active = false
        onSearch(text)
      },
      active = active,
      onActiveChange = {
        active = it
      },
      placeholder = { Text(stringResource(R.string.search_game_here)) },
      leadingIcon = {
        if (active) {
          IconButton(onClick = { active = false }) {
            Icon(
              AutoMirrored.Default.ArrowBack,
              contentDescription = null
            )
          }
        } else {
          Icon(Icons.Default.Search, contentDescription = null)
        }
      },
      content = {
        RecentSearch(searchBarUiState.recentSearches) {
          text = it
          active = false
          onSearch(it)
        }
      }
    )
  }
}

@Composable
private fun RecentSearch(
  historyQueries: List<String>,
  modifier: Modifier = Modifier,
  onSelect: (String) -> Unit
) {
  if (historyQueries.isEmpty()) {
    Column(
      modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(Icons.Default.Search, contentDescription = null)
      Text(stringResource(R.string.search_game_here))
    }
  } else {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
      items(historyQueries) { historyQuery ->
        ListItem(
          headlineContent = { Text(historyQuery) },
          leadingContent = {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              onSelect(historyQuery)
            }
        )
      }
    }
  }
}

@Composable
private fun SearchResultView(
  uiState: SearchResultUiState,
  modifier: Modifier = Modifier,
  onSelect: (SearchResultItem) -> Unit
) {
  when (uiState) {
    is Content -> {
      if (uiState.results.isEmpty()) {
        EmptyResultView(modifier)
      } else {
        ResultContentView(uiState, modifier, onSelect)
      }
    }

    Error -> ErrorResultView(modifier)
    Loading -> LoadingView(modifier)
    Initial -> InitialView(modifier)
  }
}

@Composable
private fun ResultContentView(
  content: Content,
  modifier: Modifier = Modifier,
  onSelect: (SearchResultItem) -> Unit
) {

  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val imageWidth = screenWidth / 8

  LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    item {
      Text(
        text = "Search result: ${content.results.size} items"
      )
    }
    items(content.results) { resultItem ->
      ListItem(
        headlineContent = {
          Text(resultItem.name)
        },
        leadingContent = {
          AsyncImage(
            model = resultItem.thumbnailImageUrl,
            modifier = Modifier.width(imageWidth),
            contentDescription = null
          )
        },
        supportingContent = {
          resultItem.releaseDate?.let {
            Text(it)
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSelect(resultItem) }
      )
    }
  }
}

@Composable
private fun EmptyResultView(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.not_found),
      contentDescription = null
    )
  }
}

@Composable
private fun ErrorResultView(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(64.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Default.Build,
      contentDescription = null
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Something went wrong.\nPlease try again.",
      modifier = modifier,
      style = TextStyle(textAlign = TextAlign.Center)
    )
  }
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
  LinearProgressIndicator(modifier = modifier.fillMaxWidth())
}

@Composable
private fun InitialView(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.giant_bomb),
      contentDescription = null
    )
  }
}

@Preview
@Composable
private fun PreviewSearchView() {
  GiantBombTheme {
    TopSearchBar(
      searchBarUiState = SearchBarUiState(recentSearches = listOf("Game 1", "Game 2")),
      onSearch = {}
    )

  }
}

@Preview
@Composable
private fun PreviewSearchResult() {
  GiantBombTheme {
    SearchResultView(
      uiState = Content(
        results = listOf(
          SearchResultItem(
            guid = "1",
            name = "Game 1",
            imageUrl = "https://www.giantbomb.com/image/1300-3136317",
            thumbnailImageUrl = "https://www.giantbomb.com/image/1300-3136317",
            description = "Game 1 description",
            releaseDate = "2021-01-01"
          ),
          SearchResultItem(
            guid = "2",
            name = "Game 2",
            imageUrl = "https://www.giantbomb.com/image/1300-3136317",
            thumbnailImageUrl = "https://www.giantbomb.com/image/1300-3136317",
            description = "Game 2 description",
            releaseDate = "2021-01-01"
          )
        )
      ),
      onSelect = {}
    )
  }
}

@Preview
@Composable
private fun PreviewEmptySearchHistory() {
  GiantBombTheme {
    RecentSearch(historyQueries = emptyList(), onSelect = {})
  }
}

@Preview(widthDp = 360, heightDp = 600)
@Composable
private fun PreviewErrorResult() {
  GiantBombTheme {
    ErrorResultView()
  }
}

@Preview(widthDp = 360, heightDp = 600)
@Composable
private fun PreviewLoadingSearchView() {
  GiantBombTheme {
    SearchScreen(
      uiState = SearchUiState(
        searchBarUiState = SearchBarUiState(emptyList()),
        searchResultUiState = Loading
      ),
      onSearch = {},
      onSelect = {}
    )
  }
}