package com.leic52dg17.chimp.ui.view

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.components.misc.CHANNEL_CARD_TAG
import com.leic52dg17.chimp.ui.components.misc.CHANNEL_NAME_TAG
import com.leic52dg17.chimp.ui.components.misc.LAST_MESSAGE_TAG
import com.leic52dg17.chimp.ui.views.subscribed.CREATE_CHANNEL_BUTTON_TAG
import com.leic52dg17.chimp.ui.views.subscribed.SEARCH_BAR_TAG
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubscribedChannelsViewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun subscribed_channels_correctly_displayed() {
        composeTestRule.setContent {
            SubscribedChannelsView(
                channels = testChannels,
                onChannelClick = { },
                onCreateChannelClick = { }
            )
        }

        composeTestRule.onNodeWithTag(testTag = CREATE_CHANNEL_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = CREATE_CHANNEL_BUTTON_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(testTag = CREATE_CHANNEL_BUTTON_TAG).assertIsEnabled()

        composeTestRule.onNodeWithTag(testTag = SEARCH_BAR_TAG).assertExists()
        composeTestRule.onNodeWithTag(testTag = SEARCH_BAR_TAG).assertTextContains("Search")

        for (channel in testChannels) {
            composeTestRule.onNodeWithTag(testTag = CHANNEL_CARD_TAG + "_${channel.channelId}").assertExists()

            composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG + "_${channel.channelId}", useUnmergedTree = true).assertExists()
            composeTestRule.onNodeWithTag(testTag = CHANNEL_NAME_TAG + "_${channel.channelId}", useUnmergedTree = true).assertTextEquals(channel.displayName)

            composeTestRule.onNodeWithTag(testTag = LAST_MESSAGE_TAG + "_${channel.channelId}", useUnmergedTree = true).assertExists()
            if (channel.messages.isEmpty()) {
                composeTestRule.onNodeWithTag(testTag = LAST_MESSAGE_TAG + "_${channel.channelId}", useUnmergedTree = true).assertTextEquals("")
            } else {
                composeTestRule.onNodeWithTag(testTag = LAST_MESSAGE_TAG + "_${channel.channelId}", useUnmergedTree = true).assertTextEquals(channel.messages.last().text)
            }
        }
    }


    companion object {
        private val testChannels = listOf(
            Channel(
                channelId = 1,
                displayName = "test channel 1",
                messages = listOf(
                    Message(
                        id = 1,
                        userId = 1,
                        channelId = 1,
                        text = "Message 1",
                        createdAt = 1640995200L
                    )
                ),
                users = listOf(
                    User(
                        id = 1,
                        username = "user1",
                        displayName = "User 1"
                    ),
                    User(
                        id = 2,
                        username = "user2",
                        displayName = "User 2"
                    )
                ),
                channelIconUrl = "https://picsum.photos/300/300",
                isPrivate = false,
                ownerId = 2
            ),
            Channel(
                channelId = 2,
                displayName = "test channel 2",
                messages = emptyList(),
                users = listOf(
                    User(
                        id = 1,
                        username = "user1",
                        displayName = "User 1"
                    ),
                    User(
                        id = 2,
                        username = "user2",
                        displayName = "User 2"
                    )
                ),
                channelIconUrl = "https://picsum.photos/300/300",
                isPrivate = true,
                ownerId = 1
            )
        )
    }
}