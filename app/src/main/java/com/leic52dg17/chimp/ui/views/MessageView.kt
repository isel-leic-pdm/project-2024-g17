package com.leic52dg17.chimp.ui.views


import MessageTextField
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


val My_ID = 1
val Joe_ID = 2

val messages = listOf(
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Hey!",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Hello!",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "How are you man!",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "I'm great, i'm learning PDM!",
        createdAt = 123.toBigInteger()
    )

)


@Composable
fun MessageViewLayout(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,

    ) {
    var messageText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        )

        {
            IconButton(onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.size(35.dp),
                    tint = Color.Black,
                )
            }

            Text(
                text = "Joe Biden",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 27.sp
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = "Back",
                    modifier = Modifier.size(35.dp),
                    tint = Color.Black,
                )
            }
        }

        Column(
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

            ) {
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
                Column(modifier = Modifier.padding(8.dp)) {
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
                Column(modifier = Modifier.padding(8.dp)) {
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
                .background(MaterialTheme.colorScheme.onPrimary)
                .align(Alignment.BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageResource = painterResource((R.drawable.paper_clip_icon))
            Image(
                painter = imageResource,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))


            if (messageText.isEmpty()) {

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .padding(bottom = 5.dp)
                        .padding(end = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    MessageTextField(
                        messageText = messageText,
                        onMessageTextChange = { messageText = it },
                    )
                }
            } else {

                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp)
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondary
                    ) {
                        MessageTextField(
                            messageText = messageText,
                            onMessageTextChange = { messageText = it },
                        )
                    }


                    val imageResource3 = painterResource((R.drawable.send_icon))
                    Image(
                        painter = imageResource3,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessageViewLayoutPreview() {
    MessageViewLayout(modifier = Modifier, onBackClick = {})

}