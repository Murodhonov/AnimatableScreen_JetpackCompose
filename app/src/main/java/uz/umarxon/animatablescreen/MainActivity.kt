package uz.umarxon.animatablescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.umarxon.animatablescreen.ui.theme.AnimatableScreenTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatableScreenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimateExample()
                }
            }
        }
    }
}

@Composable
private fun AnimateExample() {
    val animatedOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val offset = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            animatedOffset.animateTo(
                                offset,
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                        }
                    }
                }
            }
    ) {
        Text(stringResource(id = R.string.tap_anywhere), Modifier.align(Alignment.Center))
        Box(
            Modifier
                .offset {
                    IntOffset(
                        animatedOffset.value.x.roundToInt(),
                        animatedOffset.value.y.roundToInt()
                    )
                }
                .size(50.dp)
                .background(MaterialTheme.colors.onSecondary, CircleShape)
        ) {
            Text(
                text = "🙂",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize(),
                fontSize = 40.sp
            )
        }
    }
}