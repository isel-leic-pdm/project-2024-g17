package com.leic52dg17.chimp.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun LabelAndInputWithSuggestion(
    label: String,
    inputValue: String,
    onInfoClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    inputModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    isError: Boolean = false,
    supportingText: String = "",
    columnModifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier
    ) {
        Text(
            modifier = textModifier,
            text = stringResource(id = R.string.give_a_name_to_channel_en),
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
        InputWithSuggestion(
            value = inputValue,
            onInfoClick = {
                onInfoClick()
            },
            isError = isError,
            supportingText = supportingText,
            onValueChange = onValueChange,
            inputModifier = inputModifier
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LabelAndInputWithSuggestionPreview() {
    ChIMPTheme {
        LabelAndInputWithSuggestion(
            label = "Channel Name",
            inputValue = "",
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily!!,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight!!
        )
    }
}