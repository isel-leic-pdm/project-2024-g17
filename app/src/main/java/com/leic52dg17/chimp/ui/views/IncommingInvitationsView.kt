import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.fake.FakeData.channels
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import java.util.UUID

@Composable
fun IncommingInvitationsView(
    invitations: List<ChannelInvitation>,
    onBackClick: () -> Unit,
    onAcceptClick: (UUID) -> Unit,
    onDeclineClick: (UUID) -> Unit,
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
                        Text("Channel Name: ${channels.find { it.channelId == invitation.channelId }?.displayName}")
                        Text("Sender: ${invitation.senderId}")
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onAcceptClick(invitation.invitationId) },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Accept")
                            }
                            Button(
                                onClick = { onDeclineClick(invitation.invitationId) },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IncommingInvitationsViewPreview() {
    IncommingInvitationsView(
        invitations = FakeData.invitations,
        onBackClick = {},
        onAcceptClick = {},
        onDeclineClick = {}
    )
}