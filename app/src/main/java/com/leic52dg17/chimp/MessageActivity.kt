package com.leic52dg17.chimp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leic52dg17.chimp.ui.theme.ChIMPTheme

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = "messaging:123"

        setContent{
            ChIMPTheme {

            }
        }
    }
}