package com.leic52dg17.chimp.ui.views.user_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.ui.components.buttons.BackButton
import com.leic52dg17.chimp.ui.components.misc.Skeleton
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

@Composable
fun UserInfoLoadingView(
    isCurrentUser: Boolean,
    onBackClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (!isCurrentUser) {
                    BackButton(onBackClick = onBackClick)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Skeleton(
                    sizeModifier = Modifier
                        .size(256.dp)
                        .clip(RoundedCornerShape(10))
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Skeleton(
                        sizeModifier = Modifier
                            .width(128.dp)
                            .height(32.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )
                    Skeleton(
                        sizeModifier = Modifier
                            .width(96.dp)
                            .height(32.dp)
                    )
                }

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                if (isCurrentUser) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = false,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .width(100.dp)
                    ) {
                        Text(stringResource(R.string.logout_en))
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = false,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .width(170.dp)
                    ) {
                        Text(stringResource(R.string.change_password_text_en))
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = false,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .width(250.dp)
                    ) {
                        Text("Invite a friend to the App!")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserInfoLoadingPreview() {
    ChIMPTheme {
        UserInfoLoadingView(
            false,
            {}
        )
    }
}