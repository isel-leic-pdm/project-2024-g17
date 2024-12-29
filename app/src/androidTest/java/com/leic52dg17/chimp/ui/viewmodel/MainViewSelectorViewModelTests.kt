import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.utils.ReplaceMainDispatcherRule
import com.leic52dg17.chimp.utils.fakeAuthenticationService
import com.leic52dg17.chimp.utils.fakeChannelCacheManager
import com.leic52dg17.chimp.utils.fakeChannelInvitationService
import com.leic52dg17.chimp.utils.fakeChannelRepository
import com.leic52dg17.chimp.utils.fakeChannelService
import com.leic52dg17.chimp.utils.fakeConnectivityObserver
import com.leic52dg17.chimp.utils.fakeMessageCacheManager
import com.leic52dg17.chimp.utils.fakeMessageRepository
import com.leic52dg17.chimp.utils.fakeMessageService
import com.leic52dg17.chimp.utils.fakeRegistrationInvitationService
import com.leic52dg17.chimp.utils.fakeSseService
import com.leic52dg17.chimp.utils.fakeUserInfoRepository
import com.leic52dg17.chimp.utils.fakeUserRepository
import com.leic52dg17.chimp.utils.fakeUserService
import com.leic52dg17.chimp.utils.testAuthenticatedUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewSelectorViewModelTests {
    @get:Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Test
    fun initial_state_is_initialized() {
        // Given
        val vm = MainViewSelectorViewModel(
            authenticationService = fakeAuthenticationService,
            channelService = fakeChannelService,
            userService = fakeUserService,
            messageService = fakeMessageService,
            channelInvitationService = fakeChannelInvitationService,
            channelCacheManager = fakeChannelCacheManager,
            channelRepository = fakeChannelRepository,
            messageRepository = fakeMessageRepository,
            messageCacheManager = fakeMessageCacheManager,
            onLogout = {},
            openEmailApp = {},
            sseService = fakeSseService,
            registrationInvitationService = fakeRegistrationInvitationService,
            userInfoRepository = fakeUserInfoRepository,
            connectivityObserver = fakeConnectivityObserver,
            userRepository = fakeUserRepository
        )
        assert(vm.stateFlow.value is MainViewSelectorState.Initialized)
    }

    @Test
    fun load_channel_info_transitions_to_loading_state() =
        runTest(replaceMainDispatcherRule.testDispatcher) {
            // Given
            val vm = MainViewSelectorViewModel(
                authenticationService = fakeAuthenticationService,
                channelService = fakeChannelService,
                userService = fakeUserService,
                messageService = fakeMessageService,
                channelInvitationService = fakeChannelInvitationService,
                channelCacheManager = fakeChannelCacheManager,
                channelRepository = fakeChannelRepository,
                messageRepository = fakeMessageRepository,
                messageCacheManager = fakeMessageCacheManager,
                onLogout = {},
                openEmailApp = {},
                sseService = fakeSseService,
                registrationInvitationService = fakeRegistrationInvitationService,
                userInfoRepository = fakeUserInfoRepository,
                connectivityObserver = fakeConnectivityObserver,
                userRepository = fakeUserRepository
            )

            // When
            vm.viewModelScope.launch {
                vm.loadChannelInfo(1)
            }.join()

            // Then
            println("Current state: ${vm.stateFlow.value}")
            assert(vm.stateFlow.value is MainViewSelectorState.GettingChannelInfo)
        }

    @Test
    fun load_public_channels_transitions_to_loading_state() = runTest(replaceMainDispatcherRule.testDispatcher) {
        // Given
        val vm = MainViewSelectorViewModel(
            authenticationService = fakeAuthenticationService,
            channelService = fakeChannelService,
            userService = fakeUserService,
            messageService = fakeMessageService,
            channelInvitationService = fakeChannelInvitationService,
            channelCacheManager = fakeChannelCacheManager,
            channelRepository = fakeChannelRepository,
            messageRepository = fakeMessageRepository,
            messageCacheManager = fakeMessageCacheManager,
            onLogout = {},
            openEmailApp = {},
            sseService = fakeSseService,
            registrationInvitationService = fakeRegistrationInvitationService,
            userInfoRepository = fakeUserInfoRepository,
            connectivityObserver = fakeConnectivityObserver,
            userRepository = fakeUserRepository
        )

        // When
        vm.viewModelScope.launch {
            vm.loadPublicChannels("", 0)
        }.join()

        // Then
        println("Current state: ${vm.stateFlow.value}")
        assert(vm.stateFlow.value is MainViewSelectorState.GettingPublicChannels)
    }

    @Test
    fun accept_channel_invitation_transitions_to_accepting() = runTest(replaceMainDispatcherRule.testDispatcher) {
        // Given
        val vm = MainViewSelectorViewModel(
            authenticationService = fakeAuthenticationService,
            channelService = fakeChannelService,
            userService = fakeUserService,
            messageService = fakeMessageService,
            channelInvitationService = fakeChannelInvitationService,
            channelCacheManager = fakeChannelCacheManager,
            channelRepository = fakeChannelRepository,
            messageRepository = fakeMessageRepository,
            messageCacheManager = fakeMessageCacheManager,
            onLogout = {},
            openEmailApp = {},
            sseService = fakeSseService,
            registrationInvitationService = fakeRegistrationInvitationService,
            userInfoRepository = fakeUserInfoRepository,
            connectivityObserver = fakeConnectivityObserver,
            userRepository = fakeUserRepository
        )

        // When
        vm.viewModelScope.launch {
            vm.acceptChannelInvitation(1, testAuthenticatedUser)
        }.join()

        // Then
        println("Current state: ${vm.stateFlow.value}")
        assert(vm.stateFlow.value is MainViewSelectorState.AcceptingInvitation)
    }
}