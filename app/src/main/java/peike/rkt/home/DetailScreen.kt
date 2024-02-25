package peike.rkt.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import peike.rkt.home.HomeUiState.SearchResultItem

@Composable
fun DetailScreen(
  item: SearchResultItem,
  modifier: Modifier = Modifier,
  onBackPress: () -> Unit
) {
  BackHandler {
    onBackPress()
  }
  Card(
    modifier = modifier
      .fillMaxSize()
  ) {
    IconButton(onClick = onBackPress) {
      Icon(
        imageVector = Icons.AutoMirrored.Default.ArrowBack,
        contentDescription = null
      )
    }
    Column(modifier = Modifier.padding(16.dp)) {

      TitleRow(item = item)
      DescriptionView(item = item)
    }
  }
}

@Composable
fun TitleRow(
  item: SearchResultItem,
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier) {
    Text(
      text = item.name,
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.weight(.6f)
    )

    AsyncImage(
      model = item.imageUrl,
      modifier = Modifier.weight(.4f),
      contentScale = ContentScale.FillWidth,
      contentDescription = null
    )
  }
}

@Composable
fun DescriptionView(
  item: SearchResultItem,
  modifier: Modifier = Modifier
) {
  Text(
    text = item.description,
    modifier = modifier
  )
}

@Preview
@Composable
private fun PreviewDetailScreen() {
  DetailScreen(
    item = SearchResultItem(
      name = "Name",
      description = "Description",
      imageUrl = "https://www.giantbomb.com/a/uploads/screen_kubrick/17/171291/2670675-temp-packshot-v2.jpg",
      thumbnailImageUrl = "https://www.giantbomb.com/a/uploads/screen_kubrick/17/171291/2670675-temp-packshot-v2.jpg",
      guid = "guid"
    ),
    onBackPress = {}
  )
}