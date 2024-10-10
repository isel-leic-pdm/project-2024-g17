package com.leic52dg17.chimp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthenticationField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    leadingIconContentDescription: String = "",
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = Color.Black
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = leadingIconContentDescription
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AuthenticationPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    leadingIconContentDescription: String = "",
) {
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = Color.Black
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = leadingIconContentDescription
            )
        },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Show password"
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}