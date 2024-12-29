package com.leic52dg17.chimp.ui.view

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.views.channel.BACK_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.channel.CHANNEL_NAME_TAG
import com.leic52dg17.chimp.ui.views.channel.ChannelMessageView
import com.leic52dg17.chimp.ui.views.channel.MESSAGES_COLUMN_TAG
import com.leic52dg17.chimp.ui.views.channel.MESSAGE_BOX_TAG
import com.leic52dg17.chimp.ui.views.channel.SEND_BUTTON_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChannelMessageViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun messages_view_correctly_displays_screen_state() {
        composeTestRule.setContent {
            ChannelMessageView(
                channel,
                authenticatedUser = authenticatedUser,
                hasWritePermissions = true,
                onBackClick = { },
                onChannelNameClick = { },
                onSendClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG, useUnmergedTree = true).assertExists()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = MESSAGES_COLUMN_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).performTextInput("test message")

        composeTestRule.onNodeWithTag(testTag = SEND_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = SEND_BUTTON_TAG).assertIsEnabled()
    }

    @Test
    fun messages_view_send_button_does_not_exist_when_message_box_is_empty() {
        composeTestRule.setContent {
            ChannelMessageView(
                channel,
                authenticatedUser = authenticatedUser,
                hasWritePermissions = true,
                onBackClick = { },
                onChannelNameClick = { },
                onSendClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG, useUnmergedTree = true).assertExists()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = MESSAGES_COLUMN_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).assertTextContains("Message...")

        composeTestRule.onNodeWithTag(testTag = SEND_BUTTON_TAG).assertDoesNotExist()
    }

    @Test
    fun messages_view_send_button_disabled_when_no_write_permissions() {
        composeTestRule.setContent {
            ChannelMessageView(
                channel,
                authenticatedUser = authenticatedUser,
                hasWritePermissions = false,
                onBackClick = { },
                onChannelNameClick = { },
                onSendClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG, useUnmergedTree = true).assertExists()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = MESSAGES_COLUMN_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = MESSAGE_BOX_TAG).assertTextContains("You cannot write here...")

        composeTestRule.onNodeWithTag(testTag = SEND_BUTTON_TAG).assertDoesNotExist()
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

        private val channel = Channel(
            channelId = 1,
            displayName = "test channel",
            ownerId = 1,
            messages = emptyList(),
            users = listOf(testUser),
            isPrivate = true,
            channelIconUrl = "channel_icon_url"
        )
    }
}
