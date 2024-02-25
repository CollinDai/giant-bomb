package peike.rkt.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import peike.rkt.home.SearchUiState.SearchResultItem

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var selectedItem: SearchResultItem? by rememberSaveable { mutableStateOf(null) }
  var showDetail by rememberSaveable { mutableStateOf(false) }

  SearchScreen(
    uiState = uiState,
    modifier = modifier,
    onSearch = viewModel::search,
    onSelect = {
      selectedItem = it
      showDetail = true
    }
  )

  AnimatedVisibility(
    visible = showDetail,
    enter = slideInVertically { it },
    exit = slideOutVertically { it },
    content = {
      selectedItem?.let { item ->
        DetailScreen(
          item = item,
          modifier = modifier,
          onBackPress = { showDetail = false }
        )
      }
    }
  )
}
