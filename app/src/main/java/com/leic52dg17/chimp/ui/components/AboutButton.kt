package com.leic52dg17.chimp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun AboutButton(
    onClick: () -> Unit = {},
    text: String,
    fontSize: TextUnit,
    fontWeight: FontWeight?,
    textAlign: TextAlign,
    buttonModifier: Modifier,
    textModifier: Modifier? = null
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = buttonModifier,
        onClick = { onClick() }
    ) {
        Text(
            modifier = textModifier ?: Modifier,
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    }
}

@Preview
@Composable
fun AboutButtonPreview() {
    AboutButton(
        {},
        "Test",
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
        textAlign = TextAlign.Center,
        buttonModifier = Modifier
            .size(300.dp, 80.dp)
            .padding(bottom = 16.dp)
    )
}