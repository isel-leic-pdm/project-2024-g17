package com.leic52dg17.chimp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.ui.components.buttons.BackButton

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
        modifier = modifier.fillMaxSize()
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
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = "Incomming Invitations",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(16.dp)
            )
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
        }
    }
}
