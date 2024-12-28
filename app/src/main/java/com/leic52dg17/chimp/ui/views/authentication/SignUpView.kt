package com.leic52dg17.chimp.ui.views.authentication

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.ui.components.buttons.AuthenticationButton
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationOrDivider
import com.leic52dg17.chimp.ui.components.inputs.AuthenticationPasswordField
import com.leic52dg17.chimp.ui.components.misc.AuthenticationTitle
import com.leic52dg17.chimp.ui.components.buttons.BackButton

@Composable
fun SignUpView(
    onSignUpClick: (String, String, String, String) -> Unit,
    onLogInClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var registrationInvitation by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var displayName by rememberSaveable { mutableStateOf("") }
    var currentErrors by rememberSaveable {
        mutableStateOf<Map<String, Pair<Boolean, String?>>>(emptyMap())
    }

    fun removeErrorsForKey(key: String) {
        currentErrors = currentErrors.filterNot { it.key == key }
    }

    fun validateForm(): Boolean {

        fun isValidUUID(uuid: String): Boolean {
            val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$".toRegex()
            return uuid.matches(uuidRegex)
        }

        val errors = mutableMapOf<String, Pair<Boolean, String?>>()

        if (username.isBlank()) {
            errors["username"] = Pair(true, ErrorMessages.USERNAME_BLANK)
        }
        if (password.isBlank()) {
            errors["password"] = Pair(true, ErrorMessages.PASSWORD_BLANK)
        }
        if (displayName.isBlank()) {
            errors["displayName"] = Pair(true, ErrorMessages.DISPLAY_BLANK)
        }
        if(registrationInvitation.isBlank()) {
            errors["registrationInvitation"] = Pair(true, ErrorMessages.REGISTRATION_INVITATION_EMPTY)
        }
        if (password.length < 8 || !password.any { it.isDigit() }) {
            errors["password"] = Pair(true, ErrorMessages.PASSWORD_WEAK)
        }
        if (displayName.length < 2 || displayName.length > 50) {
            errors["displayName"] = Pair(true, ErrorMessages.DISPLAY_SIZE)
        }
        if (username.length < 2 || username.length > 50) {
            errors["username"] = Pair(true, ErrorMessages.USERNAME_SIZE)
        }
        if(!isValidUUID(registrationInvitation)) {
            errors["registrationInvitation"] = Pair(true, ErrorMessages.REGISTRATION_INVITATION_FORMAT)
        }

        currentErrors = errors

        return errors.isEmpty()
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = modifier
        ) {
            BackButton(modifier = modifier, onBackClick = onBackClick)
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            AuthenticationTitle(
                title = stringResource(R.string.sign_up_welcome_en),
                modifier = modifier,
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(bottom = 64.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                AuthenticationField(
                    value = registrationInvitation,
                    onValueChange = {
                        removeErrorsForKey("registrationInvitation")
                        registrationInvitation = it
                    },
                    label = stringResource(id = R.string.sign_up_registration_code_label_en),
                    leadingIcon = Icons.Outlined.Abc,
                    isError = currentErrors.keys.any { it == "registrationInvitation" },
                    supportingText = currentErrors["registrationInvitation"]?.second ?: ""
                )
                AuthenticationField(
                    value = username,
                    onValueChange = {
                        removeErrorsForKey("username")
                        username = it
                    },
                    label = stringResource(R.string.sign_up_username_field_label_en),
                    leadingIcon = Icons.Outlined.PersonOutline,
                    leadingIconContentDescription = stringResource(R.string.sign_up_username_icon_cd_en),
                    modifier = modifier.fillMaxWidth(),
                    isError = currentErrors.keys.any { it == "username" },
                    supportingText = currentErrors["username"]?.second ?: ""
                )
                AuthenticationField(
                    value = displayName,
                    onValueChange = {
                        removeErrorsForKey("displayName")
                        displayName = it
                    },
                    label = stringResource(R.string.sign_up_displayName_field_label_en),
                    leadingIcon = Icons.Outlined.PersonOutline,
                    leadingIconContentDescription = stringResource(R.string.sign_up_username_icon_cd_en),
                    modifier = modifier.fillMaxWidth(),
                    isError = currentErrors.keys.any { it == "displayName" },
                    supportingText = currentErrors["displayName"]?.second ?: ""
                )
                AuthenticationPasswordField(
                    value = password,
                    onValueChange = {
                        removeErrorsForKey("password")
                        password = it
                    },
                    label = stringResource(R.string.sign_up_password_field_label_en),
                    leadingIcon = Icons.Outlined.Lock,
                    leadingIconContentDescription = stringResource(R.string.sign_up_password_icon_cd_en),
                    modifier = modifier,
                    isError = currentErrors.keys.any { it == "password" },
                    supportingText = currentErrors["password"]?.second ?: ""
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                AuthenticationButton(
                    onClick = {
                        val isValid = validateForm()
                        Log.i("SIGN_UP", "Is valid? -> $isValid")
                        if (isValid) {
                            onSignUpClick(registrationInvitation, username, displayName, password)
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    text = stringResource(R.string.sign_up_button_text_en),
                )
                AuthenticationOrDivider(modifier = modifier)
                AuthenticationButton(
                    onClick = onLogInClick,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    text = stringResource(R.string.log_in_button_text_en),
                    border = BorderStroke(1.dp, Color.Gray)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpViewPreview() {
    SignUpView(
        onSignUpClick = { invitationCode, username, displayName, password ->
            println(invitationCode); println(username); println(password); println(displayName)
        },
        onLogInClick = { },
        onBackClick = { }
    )
}
