package com.leic52dg17.chimp.ui.components.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.theme.custom.bottomBorder

@Composable
fun InviteUserCard(
    user: User,
    channel: Channel,
    modifier: Modifier = Modifier,
    onInviteUserClick: (Int, Int, PermissionLevel) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .bottomBorder(0.2.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.chimp_blue_final),
                contentDescription = stringResource(id = R.string.user_icon_cd_en),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(user.displayName)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showDropdown = true }) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Add user to channel"
                )
            }
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        onInviteUserClick(
                            user.id,
                            channel.channelId,
                            PermissionLevel.RR
                        )
                        showDropdown = false
                    },
                    text = { Text("Read Only Permissions") }
                )
                DropdownMenuItem(
                    onClick = {
                        onInviteUserClick(
                            user.id,
                            channel.channelId,
                            PermissionLevel.RW
                        )
                        showDropdown = false
                    },
                    text = { Text("Read Write Permissions") }
                )
            }
        }
    }
}