package com.leic52dg17.chimp.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leic52dg17.chimp.R

@Composable
fun AuthenticationField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    leadingIconContentDescription: String = "",
    isError: Boolean = false,
    supportingText: String = ""
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
            focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            focusedTextColor = Color.Black
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = leadingIconContentDescription
            )
        },
        supportingText = {
            if(isError) {
                Text(text = supportingText, color = MaterialTheme.colorScheme.error)
            }
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
    isError: Boolean = false,
    supportingText: String = ""
) {
    var showPassword by remember { mutableStateOf(false) }
    var passwordVisibilityIcon by remember { mutableStateOf(Icons.Outlined.Visibility) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
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
                passwordVisibilityIcon = if (showPassword) {
                    Icons.Outlined.Visibility
                } else {
                    Icons.Outlined.VisibilityOff
                }

                Icon(
                    imageVector = passwordVisibilityIcon,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = stringResource(R.string.sign_up_visibility_icon_cd_en)
                )
            }
        },
        supportingText = {
            if(isError) {
                Text(text = supportingText, color = MaterialTheme.colorScheme.error)
            }
        },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
    )
}