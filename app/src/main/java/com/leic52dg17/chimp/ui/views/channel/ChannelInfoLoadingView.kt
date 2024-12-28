package com.leic52dg17.chimp.ui.views.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.misc.Skeleton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder

@Composable
fun ChannelInfoLoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 64.dp)
            ) {
                Skeleton(
                    sizeModifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Skeleton(
                    sizeModifier = Modifier
                        .width(64.dp)
                        .height(32.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Skeleton(
                sizeModifier = Modifier
                    .width(300.dp)
                    .height(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                        .topBottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
                        .heightIn(min = 300.dp, max = 300.dp)
                        .padding(bottom = 15.dp)
                ) {
                    for (i in 0..15) {
                        Row (
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        ){
                            Skeleton(
                                sizeModifier = Modifier
                                    .width(400.dp)
                                    .height(64.dp)
                                    .bottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelInfoLoadingPreview() {
    ChIMPTheme {
        ChannelInfoLoadingView()
    }
}