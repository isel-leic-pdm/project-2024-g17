package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageBarLayout() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // Content of your screen goes here

        // Message Bar at the bottom
        val shape = RoundedCornerShape(10.dp)

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            // Content of your screen goes here

            // Message Bar at the bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = shape,
                color = Color(0xFFEEEEEE), // Light gray background

            ) {
                Text(
                    text = "Message",
                    modifier = Modifier
                        .padding(20.dp),
                    color = Color.Gray,
                    fontSize = 20.sp
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