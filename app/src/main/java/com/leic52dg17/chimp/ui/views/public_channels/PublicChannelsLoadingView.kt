package com.leic52dg17.chimp.ui.views.public_channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.inputs.SearchBar
import com.leic52dg17.chimp.ui.components.misc.Skeleton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun PublicChannelsLoadingView(
    textFieldValue: String,
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            BackButton(onBackClick = { onBackClick() })
        }
        Spacer(modifier = Modifier.height(64.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                text = stringResource(id = R.string.public_channels_title_en)
            )
            SearchBar(
                textFieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                onValueChange = { value -> },
                searchValue = textFieldValue
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for(i in 0..15) {
                        Skeleton(
                            sizeModifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BackButton(
                        enabled = false,
                        onBackClick = { }
                    )
                    Skeleton(sizeModifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(10))
                    )
                    BackButton(
                        enabled = false,
                        modifier = Modifier
                            .rotate(90f),
                        onBackClick = { }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PublicChannelsLoadingPreview() {
    ChIMPTheme {
        PublicChannelsLoadingView(
            "",
            {}
        )
    }
}