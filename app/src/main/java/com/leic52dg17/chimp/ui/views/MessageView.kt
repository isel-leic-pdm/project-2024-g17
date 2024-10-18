package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.ui.components.BackButton


val My_ID = 1
val Joe_ID = 2


// fazer array de mensagens

@Composable
fun MessageViewLayout(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.onPrimary)) {

        Column(
            modifier = modifier
                .background(Color.Red)
                .padding(top = 10.dp)
        ){
            BackButton(modifier = modifier, onBackClick = onBackClick)

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )

        {
            Text(
                text = "Joe Biden",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 27.sp
            )

            Icon(
                imageVector = Icons.Filled.Circle ,
                contentDescription = "Back",
                modifier = Modifier.size(35.dp),
                tint = Color.Black,
                )
        }

        // everything after this point has to be after the topbar


        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .align(
                        Alignment.CenterHorizontally
                    ),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiary

            ){
                Text(
                    text = "10 October",
                    modifier = Modifier

                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 14.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondary
            ) {
                Column(modifier = Modifier.padding(8.dp)){
                    Text(
                        text = stringResource(R.string.message_received_en),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = "02:08 AM",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.align(Alignment.End)
                    )

                }

            }


            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 14.dp)
                    .align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Column(modifier = Modifier.padding(8.dp)){
                    Text(
                        text = stringResource(R.string.message_sent_en),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "02:55 AM",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageResource = painterResource((R.drawable.paper_clip_icon))
        Image(
                painter = imageResource,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.secondary

            ) {
                Text(
                    text = stringResource(R.string.message_text_field_en),
                    modifier = Modifier
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessageViewLayoutPreview() {
    MessageViewLayout(modifier = Modifier, onBackClick = {})
}