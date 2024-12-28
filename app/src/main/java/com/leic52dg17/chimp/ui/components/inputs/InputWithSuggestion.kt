package com.leic52dg17.chimp.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputWithSuggestion(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    inputModifier: Modifier = Modifier,
    onInfoClick: () -> Unit = {},
    inputBoxColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    isError: Boolean = false,
    supportingText: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = inputModifier,
            value = value,
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = textColor,
                containerColor = inputBoxColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = onValueChange,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                IconButton(
                    onClick = onInfoClick
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(id = R.string.information_icon_cd_en)
                    )
                }
            },
            supportingText = {
                if(isError) {
                    Text(text = supportingText, color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}

@Preview
@Composable
fun InputWithSuggestionPreview() {
    ChIMPTheme {
        InputWithSuggestion()
    }
}