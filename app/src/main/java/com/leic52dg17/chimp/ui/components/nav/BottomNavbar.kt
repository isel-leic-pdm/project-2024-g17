package com.leic52dg17.chimp.ui.components.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.NavigationIconButton
import com.leic52dg17.chimp.ui.screens.main.nav.SelectedNavIcon

@Composable
fun BottomNavbar(
    selectedIcon: SelectedNavIcon = SelectedNavIcon.Messages,
    rowModifier: Modifier? = Modifier,
    onClickProfile : () -> Unit = {},
    onClickMessages : () -> Unit = {},
    onClickAbout : () -> Unit = {},
    onClickPublic: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = rowModifier ?: Modifier
    ) {
        NavigationIconButton(
            buttonModifier = Modifier
                .weight(1f),
            iconModifier = Modifier
                .size(40.dp),
            iconTint = if(selectedIcon == SelectedNavIcon.Profile) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            onClick = { onClickProfile() },
            imageVector = Icons.Default.AccountCircle,
            contentDescription = stringResource(id = R.string.profile_icon_cd)
        )
        NavigationIconButton(
            buttonModifier = Modifier
                .weight(1f),
            iconModifier = Modifier
                .size(40.dp),
            iconTint = if(selectedIcon == SelectedNavIcon.Messages) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            onClick = { onClickMessages() },
            imageVector = Icons.Default.Email,
            contentDescription = stringResource(id = R.string.messages_icon_cd)
        )
        NavigationIconButton(
            onClick = {
                onClickPublic()
            },
            buttonModifier = Modifier
                .weight(1f),
            iconModifier = Modifier
                .size(40.dp),
            iconTint = if(selectedIcon == SelectedNavIcon.PublicChannels) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            imageVector = Icons.Outlined.Public,
            contentDescription = stringResource(id = R.string.public_channels_icon_cd)
        )
        NavigationIconButton(
            buttonModifier = Modifier
                .weight(1f),
            iconModifier = Modifier
                .size(40.dp),
            iconTint = if(selectedIcon == SelectedNavIcon.About) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            onClick = { onClickAbout() },
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(id = R.string.information_icon_cd_en)
        )
    }
}

@Preview
@Composable
fun BottomNavbarPreview() {
    BottomNavbar()
}