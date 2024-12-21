package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun ChannelCardSkeleton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Skeleton(
            sizeModifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Skeleton(
            sizeModifier = Modifier
                .height(32.dp)
                .width(256.dp)
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChannelCardSkeletonPreview() {
    ChIMPTheme {
        ChannelCardSkeleton()
    }
}