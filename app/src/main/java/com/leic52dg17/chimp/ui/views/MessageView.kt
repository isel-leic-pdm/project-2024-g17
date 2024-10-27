package com.leic52dg17.chimp.ui.views


import MessageTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.message.Message
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Date


val My_ID = 1
val Joe_ID = 2

val messages = listOf(
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Hey Joe, wanna grab a coffee?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Sure, but I'm broke. You're buying, right?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Of course, I'm the generous one. Where do you want to go?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "How about that new coffee shop downtown? They have a great selection of pastries.",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Sounds good! I'll meet you there in 30 minutes.",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Great! I'll be there waiting with a table for two.",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Cool. See you soon! \uD83D\uDE0E",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Later!",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "So, how was your coffee?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "The coffee was great, but the pastries were even better! You owe me one ",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "Deal! Next time I'm buying. Want to go see that new movie this weekend?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Sure, I'm down. Which one are you thinking of?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = My_ID,
        channelId = 1,
        text = "That sci-fi action movie everyone's been talking about. You know, the one with the robots and aliens?",
        createdAt = 123.toBigInteger()
    ),
    Message(
        userId = Joe_ID,
        channelId = 1,
        text = "Oh, that one! Yeah, I heard it's pretty good. Let's do it!",
        createdAt = 123.toBigInteger()
    )
)

fun formatDate(timestamp: BigInteger): String {

    val millis = timestamp.toLong() * 1000

    val date = Date(millis)
    val format = SimpleDateFormat("HH:mm")
    return format.format(date)
}

@Composable
fun MessageViewLayout(
    channel: Channel,
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
                        CenterHorizontally
                    ),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiary

            ) {
                Text(
                    text = "10 October",
                    modifier = Modifier
                        .weight(1f)

                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {


            items(channel.messages) { message ->
                val backgroundColor = if (message.userId == My_ID) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
                val horizontalArrangement =
                    if (message.userId == My_ID) Arrangement.End else Arrangement.Start

                val paddingVal = if (message.userId == My_ID) {
                    PaddingValues(end = 10.dp, start = 40.dp)
                } else {
                    PaddingValues(start = 10.dp, end = 40.dp)
                }

                Row(
                    modifier = Modifier
                        .padding(paddingVal)
                        .fillMaxWidth(),
                    horizontalArrangement = horizontalArrangement,

                    ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize(),

                        shape = RoundedCornerShape(8.dp),
                        color = backgroundColor
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = message.text,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = formatDate(message.createdAt),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.align(Alignment.End)
                            )

                        }

                    }
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
                        messageText = "",
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
    MessageViewLayout(
        modifier = Modifier,
        onBackClick = {},
        channel = Channel(1, "Joe Biden", messages, emptyList(), false, ""))

}