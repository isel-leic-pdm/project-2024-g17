package com.leic52dg17.chimp.ui.views.channel_invitations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun AcceptedInvitationView(
    onBackClick : () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Icon(
            modifier = Modifier
                .size(200.dp),
            imageVector = Icons.Filled.CheckCircle,
            tint = Color.Green,
            contentDescription = stringResource(id = R.string.green_check_icon_cd_en)
        )
        Spacer(
            modifier = Modifier
                .height(72.dp)
        )
        Text(
            text = "Invitation accepted!",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        )
        Spacer(
            modifier = Modifier
                .height(72.dp)
        )
        Button(
            onClick = { onBackClick() }
        ) {
            Text(text = "OK")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AcceptedInvitationPreview() {
    ChIMPTheme {
        AcceptedInvitationView {

        }
    }
}