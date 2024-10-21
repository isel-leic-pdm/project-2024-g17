package com.leic52dg17.chimp.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun NavigationIconButton(
    buttonModifier: Modifier? = Modifier,
    iconModifier: Modifier? = Modifier,
    iconTint: Color? = null,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String
) {
    IconButton(
        modifier = buttonModifier ?: Modifier,
        onClick = { onClick() }
    ) {
        Icon(
            tint = iconTint ?: Color.Unspecified,
            modifier = iconModifier ?: Modifier,
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
@Preview
fun NavigationIconButtonPreview() {
    ChIMPTheme {
        NavigationIconButton(
            onClick = { /*TODO*/ },
            imageVector = Icons.Default.AccountCircle ,
            contentDescription = "Preview"
        )
    }
}
