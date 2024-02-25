package peike.rkt.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import peike.rkt.R
import peike.rkt.home.HomeUiState.Content
import peike.rkt.home.HomeUiState.Error
import peike.rkt.home.HomeUiState.Loading
import peike.rkt.home.HomeUiState.SearchResultItem
import peike.rkt.ui.theme.GiantBombTheme

@Composable
fun SearchScreen(
  uiState: HomeUiState,
  modifier: Modifier = Modifier,
  onSearch: (String) -> Unit,
  onSelect: (SearchResultItem) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxSize()
  ) {
    TopSearchBar(
      historyQueries = uiState.searchHistory,
      onSearch = onSearch,
    )
    SearchResultView(
      uiState = uiState,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      onSelect = onSelect
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
  historyQueries: List<String>,
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
        RecentSearch(historyQueries) {
          text = it
          active = false
          onSearch(it)
        }
      }
    )
  }
}

@Composable
fun RecentSearch(
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
fun SearchResultView(
  uiState: HomeUiState,
  modifier: Modifier = Modifier,
  onSelect: (SearchResultItem) -> Unit
) {
  when (uiState) {
    is Content -> {
      if (uiState.results.isEmpty()) {
        Text("No results", modifier = modifier)
      } else {
        ResultContentView(uiState, modifier, onSelect)
      }
    }

    is Error -> ErrorResultView(modifier)
    is Loading -> LoadingView(modifier)
  }
}

@Composable
fun ResultContentView(
  content: Content,
  modifier: Modifier = Modifier,
  onSelect: (SearchResultItem) -> Unit
) {

  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(content.results) { resultItem ->
      ListItem(
        headlineContent = {
          Text(resultItem.name)
        },
        leadingContent = {
          AsyncImage(
            model = resultItem.thumbnailImageUrl,
            contentDescription = null
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onSelect(resultItem) }
      )
    }
  }
}

@Composable
fun ErrorResultView(modifier: Modifier = Modifier) {
  Text("Error", modifier = modifier)
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
  LinearProgressIndicator(modifier = modifier.padding(horizontal = 25.dp))
}

@Preview
@Composable
private fun PreviewSearchView() {
  GiantBombTheme {
    TopSearchBar(historyQueries = emptyList(), onSearch = {})
  }
}

@Preview
@Composable
private fun PreviewSearchResult() {
  GiantBombTheme {
    SearchResultView(
      uiState = Content(
        searchHistory = emptyList(),
        results = listOf(
          SearchResultItem(
            guid = "1",
            name = "Game 1",
            imageUrl = "https://www.giantbomb.com/image/1300-3136317",
            thumbnailImageUrl = "https://www.giantbomb.com/image/1300-3136317",
            description = "Game 1 description"
          ),
          SearchResultItem(
            guid = "2",
            name = "Game 2",
            imageUrl = "https://www.giantbomb.com/image/1300-3136317",
            thumbnailImageUrl = "https://www.giantbomb.com/image/1300-3136317",
            description = "Game 2 description"
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