package peike.rkt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import peike.rkt.home.HomeScreen
import peike.rkt.home.HomeViewModel
import peike.rkt.ui.theme.GiantBombTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel: HomeViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()
    setContent {
      GiantBombTheme {
        HomeScreen(viewModel)
      }
    }
  }
}
