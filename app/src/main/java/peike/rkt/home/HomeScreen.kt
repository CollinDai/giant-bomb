package peike.rkt.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import peike.rkt.home.HomeUiState.SearchResultItem

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var selectedItem: SearchResultItem? by rememberSaveable { mutableStateOf(null) }

  when (val item = selectedItem) {
    null -> SearchScreen(
      uiState = uiState,
      onSearch = viewModel::search,
      onSelect = { selectedItem = it }
    )

    else -> DetailScreen(
      item = item,
      onBackPress = { selectedItem = null }
    )
  }
}

