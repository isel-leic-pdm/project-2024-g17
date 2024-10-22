package com.leic52dg17.chimp.ui.components.toggles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun ToggleWithLabel(
    text: String = "",
    textModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    switchModifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = boxModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = textModifier,
            text = text
        )
        Switch(
            modifier = switchModifier,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
fun ToggleWithLabelPreview() {
    ChIMPTheme {
        ToggleWithLabel(
            text = "State 1"
        )
    }
}