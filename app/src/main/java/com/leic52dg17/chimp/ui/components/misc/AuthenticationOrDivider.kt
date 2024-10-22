package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthenticationOrDivider(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        HorizontalDivider(color = Color.Gray, modifier = modifier.weight(1f))
        Text(
            text = "or",
            modifier = modifier.padding(horizontal = 8.dp),
            fontSize = 16.sp,
            color = Color.Gray
        )
        HorizontalDivider(color = Color.Gray, modifier = modifier.weight(1f))
    }
}
