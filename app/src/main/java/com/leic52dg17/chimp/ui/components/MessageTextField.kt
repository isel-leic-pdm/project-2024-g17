package com.leic52dg17.chimp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leic52dg17.chimp.R

// Component with message box


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBarLayout(modifier: Modifier = Modifier) {
    var messageText by remember { mutableStateOf("") }

    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack, // Replace with your desired arrow icon
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.size(24.dp) // Adjust icon size as needed
        )
        Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed

        //OutlinedTextField instead of Surface
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text(stringResource(R.string.message_text_field_en)) },
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                //textColor = MaterialTheme.colorScheme.onSecondary,
                //backgroundColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}



