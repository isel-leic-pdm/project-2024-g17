package com.leic52dg17.chimp.ui.views.channel_invitations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun IncomingInvitationsView(
    invitations: List<ChannelInvitationDetails>,
    onBackClick: () -> Unit,
    onAcceptClick: (Int) -> Unit,
    onDeclineClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            BackButton(onBackClick = onBackClick)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
        ) {
            Column {
                Text(
                    text = "Incoming Invitations",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(16.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if(invitations.isNotEmpty()) {
                    for (invitation in invitations) {
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Text that GETS channel name through channelId
                                Text("Channel Name: ${invitation.channelName}")
                                Text("Sender: ${invitation.senderUsername}")
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = { onAcceptClick(invitation.id) },
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Accept")
                                    }
                                    Button(
                                        onClick = { onDeclineClick(invitation.id) },
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("Decline")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(id = R.string.no_invitations_en)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IncomingInvitationsPreview() {
    ChIMPTheme {
        IncomingInvitationsView(
            invitations = emptyList(),
            onBackClick = { /*TODO*/ },
            onAcceptClick = {},
            onDeclineClick = {}
        )
    }
}
