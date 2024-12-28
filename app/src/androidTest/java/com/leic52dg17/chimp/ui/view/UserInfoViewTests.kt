package com.leic52dg17.chimp.ui.view

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BACK_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.user_info.CHANGE_PASSWORD_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.user_info.CHANNEL_INVITATIONS_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.user_info.LOGOUT_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.user_info.REGISTRATION_INVITATION_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.user_info.USER_DISPLAY_NAME_TAG
import com.leic52dg17.chimp.ui.views.user_info.USER_PROFILE_PICTURE_TAG
import com.leic52dg17.chimp.ui.views.user_info.USER_USERNAME_TAG
import com.leic52dg17.chimp.ui.views.user_info.UserInfoView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserInfoViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun user_info_is_correctly_displayed_when_profile_belongs_to_current_user() {
        composeTestRule.setContent {
            UserInfoView(
                user = testUser,
                isCurrentUser = true,
                onBackClick = { },
                onChangePasswordClick = { },
                onInvitationsClick = { },
                onLogoutClick = { },
                onRegistrationInvitationClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(testTag = USER_PROFILE_PICTURE_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = USER_DISPLAY_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_DISPLAY_NAME_TAG).assertTextEquals(testUser.displayName)

        composeTestRule.onNodeWithTag(testTag = USER_USERNAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_USERNAME_TAG).assertTextEquals("@" + testUser.username)

        composeTestRule.onNodeWithTag(testTag = LOGOUT_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = LOGOUT_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = LOGOUT_BUTTON_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = CHANGE_PASSWORD_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CHANGE_PASSWORD_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CHANGE_PASSWORD_BUTTON_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = REGISTRATION_INVITATION_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = REGISTRATION_INVITATION_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = REGISTRATION_INVITATION_BUTTON_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_INVITATIONS_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_INVITATIONS_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_INVITATIONS_BUTTON_TAG).assertIsEnabled()
    }

    @Test
    fun user_info_is_correctly_displayed_when_profile_does_not_belong_to_current_user() {
        composeTestRule.setContent {
            UserInfoView(
                user = testUser,
                isCurrentUser = false,
                onBackClick = { },
                onChangePasswordClick = { },
                onInvitationsClick = { },
                onLogoutClick = { },
                onRegistrationInvitationClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = USER_PROFILE_PICTURE_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = USER_DISPLAY_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_DISPLAY_NAME_TAG).assertTextEquals(testUser.displayName)

        composeTestRule.onNodeWithTag(testTag = USER_USERNAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_USERNAME_TAG).assertTextEquals("@" + testUser.username)

        composeTestRule.onNodeWithTag(testTag = LOGOUT_BUTTON_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(testTag = CHANGE_PASSWORD_BUTTON_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(testTag = REGISTRATION_INVITATION_BUTTON_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_INVITATIONS_BUTTON_TAG).assertDoesNotExist()
    }

    companion object {
        private val testUser = User(
            id = 1,
            username = "test_username",
            displayName = "test display name"
        )

        private val authenticatedUser = AuthenticatedUser(
            authenticationToken = "token",
            user = testUser
        )
    }
}