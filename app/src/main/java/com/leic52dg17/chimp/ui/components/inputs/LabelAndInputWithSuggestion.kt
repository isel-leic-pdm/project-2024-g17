package com.leic52dg17.chimp.ui.components.inputs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun LabelAndInputWithSuggestion(
    label: String,
    inputValue: String,
    onInfoClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    inputModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Text(
        modifier = textModifier,
        text = stringResource(id = R.string.give_a_name_to_channel_en),
        textAlign = TextAlign.Center,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
    )
    InputWithSuggestion(
        value = inputValue,
        onInfoClick = {
            onInfoClick()
        },
        onValueChange = onValueChange,
        inputModifier = inputModifier
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LabelAndInputWithSuggestionPreview() {
    ChIMPTheme {
        LabelAndInputWithSuggestion(
            label = "Channel Name",
            inputValue = "",
        )
    }
}