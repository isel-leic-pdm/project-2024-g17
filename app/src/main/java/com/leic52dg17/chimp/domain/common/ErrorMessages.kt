package com.leic52dg17.chimp.domain.common

object ErrorMessages {
    const val UNKNOWN = "An unknown error has occurred. Please try again later."
    const val USER_NOT_FOUND = "The requested user could not be found."
    const val CHANNEL_NOT_FOUND = "The requested channel could not be found."
    const val AUTHENTICATED_USER_NULL = "Error retrieving information about the current authenticated user.\nYou'll now be logged out."
    const val UNAUTHORIZED = "You are unauthorized to perform these action.\nPlease sign in again."
    const val USER_NOT_IN_CHANNEL = "The requested user is not in the requested channel."
    const val INDEXING_ERROR = "An unknown indexing error has occurred."
    const val ID = "IDs must be greater or equal to one."
    const val DISPLAY_BLANK = "Display name cannot be blank."
    const val DISPLAY_EMPTY = "Display name cannot be empty."
    const val DISPLAY_TOO_LONG = "Display name length cannot exceed 50 characters."
    const val CHANNEL_ICON_URL_EMPTY = "Channel icon URL cannot be empty."
    const val CHANNEL_ICON_URL_BLANK = "Channel icon URL cannot be blank."
    const val JOINED_AT = "Joined at timestamp must be greater than zero."
    const val TEXT_EMPTY = "Message text cannot be empty."
    const val USERNAME_EMPTY = "Username cannot be empty."
    const val USERNAME_BLANK = "Username cannot be blank."
    const val USERNAME_TOO_LONG = "Username length cannot exceed 50 characters."
    const val LISTEN_JOB_ALREADY_ACTIVE = "Listen job is still active, and thus a new one cannot be started."
}