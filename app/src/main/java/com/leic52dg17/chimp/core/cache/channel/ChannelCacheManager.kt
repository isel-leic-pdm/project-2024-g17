package com.leic52dg17.chimp.core.cache.channel

import android.util.Log
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.common.ServiceException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChannelCacheManager(
    private val channelRepository: IChannelRepository,
    private val channelService: IChannelService
) : IChannelCacheManager {
    private var successCallback: ((newChannels: List<Channel>) -> Unit)? = {}
    private var errorCallback: ((errorMessage: String) -> Unit)? = {}
    private val _currentChannels = MutableStateFlow<List<Channel>>(emptyList())
    private var collectionJob: Job? = null

    // Set the interval for auto cache update
    override suspend fun startCollection(userId: Int) {
        collectionJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    val storedChannels = channelRepository.getStoredChannels()
                    val newChannels = channelService.getUserSubscribedChannels(userId)
                    val hasChanged = channelRepository.isUpdateDue(storedChannels, newChannels)
                    if (hasChanged) {
                        _currentChannels.emit(newChannels)
                        runCallback()
                    }
                } catch (e: ServiceException) {
                    runErrorCallback(e.message)
                } catch (e: Exception) {
                    runErrorCallback(ErrorMessages.UNKNOWN)
                }
                delay(10000)
            }
        }
    }

    // Cancels the collection job, effectively stopping updates to the cache
    override suspend fun stopCollection() {
        collectionJob?.cancel()
        collectionJob = null
    }

    // Forces a cache update (i.e. when creating a channel)
    override suspend fun forceUpdate(userId: Int) {
        try {
            val storedChannels = channelRepository.getStoredChannels()
            val newChannels = channelService.getUserSubscribedChannels(userId)
            val hasChanged = channelRepository.isUpdateDue(storedChannels, newChannels)
            if (hasChanged) {
                _currentChannels.emit(newChannels)
                runCallback()
            }
        } catch (e: ServiceException) {
            runErrorCallback(e.message)
        } catch (e: Exception) {
            runErrorCallback(ErrorMessages.UNKNOWN)
        }
    }

    // Registers a callback to be run on cache update
    override suspend fun registerCallback(callback: (newChannels: List<Channel>) -> Unit) {
        Log.i(TAG, "Registering callback...")
        successCallback = callback
        Log.i(TAG, "Registered callback!")
    }

    // Registers a callback to be run on error
    override suspend fun registerErrorCallback(callback: (errorMessage: String) -> Unit) {
        Log.i(TAG, "Registering error callback...")
        errorCallback = callback
        Log.i(TAG, "Registered error callback!")
    }

    // Clears the callback
    override suspend fun unregisterCallbacks() {
        Log.i(TAG, "Unregistering callbacks...")
        successCallback = null
        errorCallback = null
        Log.i(TAG, "Unregistered callbacks!")
    }

    // Runs the callback, if it's set.
    override suspend fun runCallback() {
        Log.i(TAG, "Running callback...")
        if (successCallback == null) {
            Log.w(TAG, "There is no callback registered!!!")
            return
        } else successCallback?.invoke(_currentChannels.value)
        Log.i(TAG, "Ran callback!")
    }

    // Runs the error callback, if it's set
    override suspend fun runErrorCallback(errorMessage: String) {
        Log.i(TAG, "Running error callback...")
        if (errorCallback == null) {
            Log.w(TAG, "There is no callback registered!!!")
            return
        } else errorCallback?.invoke(errorMessage)
        Log.i(TAG, "Ran error callback!")
    }

    companion object {
        const val TAG = "CHANNEL_CACHE_MANAGER"
    }
}