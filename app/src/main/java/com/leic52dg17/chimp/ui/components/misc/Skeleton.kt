package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun Skeleton(
    sizeModifier: Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite skeleton transition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ), label = "Skeleton animation"
    )

    Box(
        modifier = sizeModifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Gray.copy(alpha = 0.3f),
                        Color.Gray.copy(alpha = 0.5f),
                        Color.Gray.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .alpha(alpha)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SkeletonPreview() {
    ChIMPTheme {
        Skeleton(
            sizeModifier = Modifier
                .height(50.dp)
                .width(200.dp)
        )
    }
}