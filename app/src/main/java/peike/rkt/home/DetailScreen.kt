package peike.rkt.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import peike.rkt.home.SearchUiState.SearchResultItem

@Composable
fun DetailScreen(
  item: SearchResultItem,
  modifier: Modifier = Modifier,
  onBackPress: () -> Unit
) {

  val configuration = LocalConfiguration.current

  val screenWidth = configuration.screenWidthDp.dp

  val imageWidth = screenWidth / 2

  BackHandler {
    onBackPress()
  }
  Card(
    modifier = modifier
      .padding(top = 16.dp)
      .fillMaxSize()
  ) {
    IconButton(onClick = onBackPress) {
      Icon(
        imageVector = Icons.AutoMirrored.Default.ArrowBack,
        contentDescription = null
      )
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      SubcomposeAsyncImage(
        model = item.imageUrl,
        loading = {
          Box(
            modifier = Modifier
              .heightIn(max = imageWidth)
              .fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {

            CircularProgressIndicator()
          }
        },
        modifier = Modifier
          .width(imageWidth)
          .align(Alignment.CenterHorizontally),
        contentDescription = null
      )
      Spacer(modifier = Modifier.height(16.dp))
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

  val title = item.releaseDate?.let { releaseDate ->
    "${item.name} ($releaseDate)"
  } ?: item.name

  Text(
    text = title,
    modifier = modifier,
    style = MaterialTheme.typography.headlineSmall
  )
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
      guid = "guid",
      releaseDate = "2021-10-08"
    ),
    onBackPress = {}
  )
}