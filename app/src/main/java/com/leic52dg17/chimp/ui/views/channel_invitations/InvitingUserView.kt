package com.leic52dg17.chimp.ui.views.channel_invitations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import kotlinx.coroutines.delay

private val loadingPhrases = listOf(
    "Telling our mail chimps to warn your friend...",
    "Getting the invitation to it's destination",
    "We're working on it...",
    "Wait for it..."
)

@Composable
fun InvitingUserView(
    userDisplayName: String
) {
    var currentPhrase by rememberSaveable {
        mutableStateOf(loadingPhrases[0])
    }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentPhrase = loadingPhrases[currentIndex]
            currentIndex = (currentIndex + 1) % loadingPhrases.size
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(128.dp)
        )
        Spacer(
            modifier = Modifier
                .height(72.dp)
        )
        Text(
            text = "Inviting $userDisplayName...",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        )
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )
        Text(
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            color = MaterialTheme.colorScheme.secondary,
            text = currentPhrase
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatingChannelPreview() {
    ChIMPTheme {
        InvitingUserView(
            "User"
        )
    }
}