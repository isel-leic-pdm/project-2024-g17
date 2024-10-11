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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R

@Composable
fun MessageBarLayout() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )

        {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew ,
                contentDescription = "Back",
                tint = Color.Black
            )
            Text(
                text = "Joe Biden",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 27.sp
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.LightGray, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))



        Column (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .align(
                        Alignment.CenterHorizontally
                    ),
                shape = RoundedCornerShape(12.dp),
                color = Color.Gray

            ){
                Text(
                    text = "10 October",
                    modifier = Modifier

                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Surface(
                modifier = Modifier
                    .wrapContentSize(),

                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray
            ) {
                Column(modifier = Modifier.padding(8.dp)){
                    Text(
                        text = "Hey man, how are you?",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black
                    )
                    Text(
                        text = "02:08 AM",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )

                }

            }


            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                color = Color.Blue
            ) {
                Column(modifier = Modifier.padding(8.dp)){
                    Text(
                        text = "I'm doing well bro, thanks!",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                    Text(
                        text = "02:55 AM",
                        fontSize = 12.sp,
                        color = Color.Gray,
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
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEEEEEE),

            ) {
                Text(
                    text = "Message",
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.LightGray,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessageBarLayoutPreview() {
    MessageBarLayout()
}