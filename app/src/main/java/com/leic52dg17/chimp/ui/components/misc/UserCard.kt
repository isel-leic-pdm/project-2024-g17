package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder

@Composable
fun UserCard(
    user: User = User(1, "test_user", "Test User")
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.chimp_blue_final),
                contentDescription = user.username + " profile picture",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Text(
                text = user.displayName,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            )
        }

        Text(
            text = '@' + user.username,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
        )
        IconButton(
            onClick = { /*TODO*/ })
        {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = "Add user to channel"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    UserCard()
}