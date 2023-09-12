package com.astune.core.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun ClippedBackground(
    modifier: Modifier = Modifier,
    offset: Offset,
    backgroundImg: ImageBitmap = ImageBitmap(0, 0),
    backgroundSize: Size,
){
    var srcOffset by remember { mutableStateOf(-offset) }

    Box(modifier = modifier.onGloballyPositioned {
        srcOffset = it.positionInRoot() - offset
        //Log.i("background", "" + IntOffset(srcOffset.x.toInt(), srcOffset.y.toInt()))
        //Log.i("background", "root position$offset")
    }.fillMaxSize()){
        Canvas(modifier = Modifier)
        {
            val imageSize = Size(backgroundImg.width.toFloat(), backgroundImg.height.toFloat())
            val scale = max(backgroundSize.width / imageSize.width, backgroundSize.height / imageSize.height)
            val dx = ((backgroundSize.width - imageSize.width * scale) / 2f ).roundToInt()
            val dy = ((backgroundSize.height - imageSize.height * scale) / 2f ).roundToInt()
            //Log.i("canvas", "drawing")
            drawImage(
                image = backgroundImg,
                dstOffset = IntOffset(dx, dy) - IntOffset(srcOffset.x.toInt(), srcOffset.y.toInt()),
                dstSize = IntSize((imageSize.width * scale).roundToInt(), (imageSize.height * scale).roundToInt())
            )
        }
    }
}

@Composable
fun CaptureBitmap(
    key:Boolean,
    onBitmapCaptured : (Bitmap) -> Unit,
    content: @Composable () -> Unit,
) {

    val context = LocalContext.current

    /**
     * ComposeView that would take composable as its content
     * Kept in remember so recomposition doesn't re-initialize it
     **/
    val composeView = remember { ComposeView(context) }


    LaunchedEffect(key) {
        composeView.post {
            onBitmapCaptured.invoke(composeView.drawToBitmap())
        }
    }

    /** Use Native View inside Composable **/
    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        }
    )
}
