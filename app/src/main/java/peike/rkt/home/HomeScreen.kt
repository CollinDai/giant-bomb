package peike.rkt.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import peike.rkt.home.HomeUiState.Content
import peike.rkt.home.HomeUiState.Error
import peike.rkt.home.HomeUiState.Loading
import peike.rkt.ui.theme.GiantBombTheme

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Column(modifier = modifier.fillMaxSize()) {
    SearchView(
      onSearch = viewModel::search
    )
    SearchResultView(
      uiState, modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
  modifier: Modifier = Modifier,
  onSearch: (String) -> Unit = {}
) {
  var text by rememberSaveable { mutableStateOf("") }
  var active by rememberSaveable { mutableStateOf(false) }

  Box(
    Modifier
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
      placeholder = { Text("Search game here") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    ) {
      repeat(4) { idx ->
        val resultText = "Suggestion $idx"
        ListItem(
          headlineContent = { Text(resultText) },
          supportingContent = { Text("Additional info") },
          leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
          modifier = Modifier
            .clickable {
              text = resultText
              active = false
            }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
        )
      }
    }
  }
}

@Composable
fun SearchResultView(
  uiState: HomeUiState,
  modifier: Modifier = Modifier
) {
//  ErrorResultView(modifier)
  when (uiState) {
    is Content -> {
      if (uiState.results.isEmpty()) {
        Text("No results", modifier = modifier)
      } else {
        ResultContentView(uiState, modifier)
      }
    }

    is Error -> ErrorResultView(modifier)
    Loading -> LoadingView(modifier)
  }
}

@Composable
fun ResultContentView(
  content: Content,
  modifier: Modifier = Modifier
) {

  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(content.results) {
      ListItem(headlineContent = {
        Text(it.name)
      })
    }
  }
}

@Composable
fun ErrorResultView(modifier: Modifier = Modifier) {
  Text("Error", modifier = modifier)
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
  LinearProgressIndicator(modifier = modifier)
}

@Preview
@Composable
private fun PreviewSearchView() {
  GiantBombTheme {
    SearchView()
  }
}

@Preview
@Composable
private fun PreviewSearchResultEmpty() {
  GiantBombTheme {
    SearchResultView(HomeUiState.Content(emptyList()))
  }
}

@Preview
@Composable
private fun PreviewSearchResultLoading() {
  GiantBombTheme {
    SearchResultView(Loading)
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  GiantBombTheme {
    Greeting("Android")
  }
}