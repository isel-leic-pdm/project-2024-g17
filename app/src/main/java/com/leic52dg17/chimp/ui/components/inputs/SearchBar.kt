package com.leic52dg17.chimp.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    textFieldModifier: Modifier = Modifier,
    placeHolderFontSize: TextUnit = 16.sp,
    placeHolderFontWeight: FontWeight = FontWeight.Medium,
    onValueChange : (String) -> Unit,
    searchValue: String,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    TextField(
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            color = MaterialTheme.colorScheme.onBackground
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = color,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = textFieldModifier,
        shape = RoundedCornerShape(10.dp),
        value = searchValue,
        placeholder = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_icon_cd)
                )
                Text(
                    fontSize = placeHolderFontSize,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontWeight = placeHolderFontWeight,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.subscribed_channels_search_bar_text_en)
                )
            }
        },
        onValueChange = onValueChange
    )
}