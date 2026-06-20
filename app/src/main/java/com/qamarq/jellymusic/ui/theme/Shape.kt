package com.qamarq.jellymusic.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object ElovaireRadii {
    val topBar: Dp = 0.dp
    val dock: Dp = 20.dp
    val module: Dp = 20.dp
    val card: Dp = 16.dp
    val tile: Dp = 16.dp
    val button: Dp = 18.dp
    val artwork: Dp = 12.dp
    val artworkSmall: Dp = 8.dp
    val input: Dp = 18.dp
    val dialog: Dp = 24.dp
    val pill: Dp = 999.dp
}

fun elovaireShapes(): Shapes {
    return Shapes(
        extraSmall = RoundedCornerShape(ElovaireRadii.tile),
        small = RoundedCornerShape(ElovaireRadii.tile),
        medium = RoundedCornerShape(ElovaireRadii.card),
        large = RoundedCornerShape(ElovaireRadii.module),
        extraLarge = RoundedCornerShape(ElovaireRadii.dock),
    )
}
