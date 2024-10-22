package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing,
        modifier = modifier
            .padding(horizontal = 16.dp)
    )
}