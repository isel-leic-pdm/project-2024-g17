package com.leic52dg17.chimp.model.common

object ErrorMessages {
    const val USER_NOT_FOUND = "The requested user could not be found."
    const val CHANNEL_NOT_FOUND = "The requested channel could not be found."
    const val AUTHENTICATED_USER_NULL = "Error retrieving information about the current authenticated user."
    const val USER_NOT_IN_CHANNEL = "The requested user is not in the requested channel."
    const val INDEXING_ERROR = "An unknown indexing error has occurred."
    const val ID = "IDs must be greater or equal to one."
    const val DISPLAY_BLANK = "Display name cannot be blank."
    const val DISPLAY_EMPTY = "Display name cannot be empty."
    const val CHANNEL_ICON_URL_EMPTY = "Channel icon URL cannot be empty."
    const val CHANNEL_ICON_URL_BLANK = "Channel icon URL cannot be blank."
    const val JOINED_AT = "Joined at timestamp must be greater than zero."
    const val TEXT_EMPTY = "Message text cannot be empty."
    const val USERNAME_EMPTY = "Username cannot be empty."
    const val USERNAME_BLANK = "Username cannot be blank."
}