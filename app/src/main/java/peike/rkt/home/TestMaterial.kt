package peike.rkt.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun TestAnimateSize(modifier: Modifier = Modifier) {
  val shortText = "Hi"
  val longText = "Very long text\nthat spans across\nmultiple lines"
  var short by remember { mutableStateOf(true) }
  Box(
    modifier = Modifier
      .background(
        Color.Blue,
        RoundedCornerShape(15.dp)
      )
      .clickable { short = !short }
      .padding(20.dp)
      .wrapContentSize()
      .animateContentSize()
  ) {
    Text(
      if (short) {
        shortText
      } else {
        longText
      },
      style = LocalTextStyle.current.copy(color = Color.White)
    )
  }
}

@Composable
fun TestAnimatedVisibility(modifier: Modifier = Modifier) {
  var visible by remember { mutableStateOf(true) }

  Column {

    AnimatedVisibility(
      visible,
      enter = expandIn(
        // Overwrites the default spring animation with tween
        animationSpec = tween(100, easing = LinearOutSlowInEasing),
        // Overwrites the corner of the content that is first revealed
        expandFrom = Alignment.BottomStart
      ) {
        // Overwrites the initial size to 50 pixels by 50 pixels
        IntSize(50, 50)
      },
      exit = shrinkOut(
        tween(100, easing = FastOutSlowInEasing),
        // Overwrites the area of the content that the shrink animation will end on. The
        // following parameters will shrink the content's clip bounds from the full size of the
        // content to 1/10 of the width and 1/5 of the height. The shrinking clip bounds will
        // always be aligned to the CenterStart of the full-content bounds.
        shrinkTowards = Alignment.CenterStart
      ) { fullSize ->
        // Overwrites the target size of the shrinking animation.
        IntSize(fullSize.width / 10, fullSize.height / 5)
      }
    ) {
      // Content that needs to appear/disappear goes here:
      Text("Content to appear/disappear",
        Modifier
          .fillMaxWidth()
          .requiredHeight(200.dp))
    }
    Text(text = "Click me to toggle visibility", modifier.clickable { visible = !visible })
  }

}

@Preview
@Composable
private fun PreviewAnimateSize() {
  TestAnimateSize()
}

@Preview
@Composable
private fun PreviewTestAnimatedVisibility() {
  TestAnimatedVisibility()
}