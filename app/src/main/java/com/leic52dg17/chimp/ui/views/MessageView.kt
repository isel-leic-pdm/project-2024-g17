package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    .size(25.dp)
                    .background(Color.Gray, CircleShape)
            )
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