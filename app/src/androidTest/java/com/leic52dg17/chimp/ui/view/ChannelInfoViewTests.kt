package com.leic52dg17.chimp.ui.view

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.buttons.BACK_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.channel.ADD_USER_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.channel.CHANNEL_IMAGE_TAG
import com.leic52dg17.chimp.ui.views.channel.ChannelInfoView
import com.leic52dg17.chimp.ui.views.channel.LEAVE_CHANNEL_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.channel.REMOVE_USER_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.channel.USERS_LIST_TAG
import com.leic52dg17.chimp.ui.views.channel.USER_CARD_DISPLAY_NAME_TAG
import com.leic52dg17.chimp.ui.views.channel.USER_CARD_TAG
import com.leic52dg17.chimp.ui.views.channel.USER_INFO_CHANNEL_NAME_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChannelInfoViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun channel_info_is_correctly_displayed_when_channel_belongs_to_current_user() {
        composeTestRule.setContent {
            ChannelInfoView(
                channel = testChannel1,
                authenticatedUser = testAuthenticatedUser
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_IMAGE_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = USER_INFO_CHANNEL_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_INFO_CHANNEL_NAME_TAG).assertTextEquals(testChannel1.displayName)

        composeTestRule.onNodeWithTag(testTag = USERS_LIST_TAG).assertExists()

        for (user in testChannel1.users) {
            composeTestRule.onNodeWithTag(testTag = USER_CARD_TAG + "_${user.id}").assertExists()

            composeTestRule.onNodeWithTag(testTag = USER_CARD_DISPLAY_NAME_TAG + "_${user.id}", useUnmergedTree = true).assertExists()
            composeTestRule.onNodeWithTag(testTag = USER_CARD_DISPLAY_NAME_TAG + "_${user.id}", useUnmergedTree = true).assertTextEquals(user.displayName)

            if (user.id != testAuthenticatedUser.user?.id) {
                composeTestRule.onNodeWithTag(testTag = REMOVE_USER_BUTTON_TAG + "_${user.id}", useUnmergedTree = true).assertExists()
                composeTestRule.onNodeWithTag(testTag = REMOVE_USER_BUTTON_TAG + "_${user.id}", useUnmergedTree = true).assertHasClickAction()
                composeTestRule.onNodeWithTag(testTag = REMOVE_USER_BUTTON_TAG + "_${user.id}", useUnmergedTree = true).assertIsEnabled()
            }
        }

        composeTestRule.onNodeWithTag(testTag = ADD_USER_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = ADD_USER_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = ADD_USER_BUTTON_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertIsEnabled()
    }

    @Test
    fun channel_info_is_correctly_displayed_when_channel_does_not_belong_to_current_user() {
        composeTestRule.setContent {
            ChannelInfoView(
                channel = testChannel2,
                authenticatedUser = testAuthenticatedUser
            )
        }

        composeTestRule.onNodeWithTag(testTag = BACK_BUTTON_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = CHANNEL_IMAGE_TAG).assertExists()

        composeTestRule.onNodeWithTag(testTag = USER_INFO_CHANNEL_NAME_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = USER_INFO_CHANNEL_NAME_TAG).assertTextEquals(testChannel2.displayName)

        composeTestRule.onNodeWithTag(testTag = USERS_LIST_TAG).assertExists()

        for (user in testChannel2.users) {
            composeTestRule.onNodeWithTag(testTag = USER_CARD_TAG + "_${user.id}").assertExists()

            composeTestRule.onNodeWithTag(testTag = USER_CARD_DISPLAY_NAME_TAG + "_${user.id}", useUnmergedTree = true).assertExists()
            composeTestRule.onNodeWithTag(testTag = USER_CARD_DISPLAY_NAME_TAG + "_${user.id}", useUnmergedTree = true).assertTextEquals(user.displayName)

            if (user.id != testAuthenticatedUser.user?.id) {
                composeTestRule.onNodeWithTag(testTag = REMOVE_USER_BUTTON_TAG + "_${user.id}", useUnmergedTree = true).assertDoesNotExist()
            }
        }

        composeTestRule.onNodeWithTag(testTag = ADD_USER_BUTTON_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = LEAVE_CHANNEL_BUTTON_TAG).assertIsEnabled()
    }

    companion object {
        private val testUsers = listOf(
            User(
                id = 1,
                username = "test_user_1",
                displayName = "TestUser1"
            ),
            User(
                id = 2,
                username = "test_user_2",
                displayName = "TestUser2"
            )
        )

        private val testAuthenticatedUser = AuthenticatedUser(
            authenticationToken = "token",
            user = testUsers[0],
            tokenExpirationDate = null
        )

        private val testChannel1 = Channel(
            channelId = 1,
            ownerId = 1,
            displayName = "test channel 1",
            isPrivate = true,
            users = testUsers,
            messages = emptyList(),
            channelIconUrl = "channel icon url",
        )

        private val testChannel2 = Channel(
            channelId = 2,
            ownerId = 2,
            displayName = "test channel 2",
            isPrivate = true,
            users = testUsers,
            messages = emptyList(),
            channelIconUrl = "channel icon url",
        )
    }
}