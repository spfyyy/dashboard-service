package com.dylantjohnson.dashboardservice.credentials

import com.dylantjohnson.dashboardservice.Config
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CredentialsHandlerTests {
    companion object {
        private const val USERNAME = "testuser"
        private const val PASSWORD = "testpassword"
    }

    @Mock private lateinit var mConfig: Config

    private lateinit var mCredentialsHandler: CredentialsHandler

    @BeforeEach
    fun initialize() {
        mCredentialsHandler = CredentialsHandler(mConfig)
    }

    @Test
    fun shouldAllowValidCredentials() {
        given(mConfig.allowedUsername).willReturn(USERNAME)
        given(mConfig.allowedPassword).willReturn(PASSWORD)
        assert(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD))
    }

    @Test
    fun shouldNotAllowInvalidUsername() {
        given(mConfig.allowedUsername).willReturn(USERNAME)
        assert(!mCredentialsHandler.credentialsAreValid("wrongusername", "wrongpassword"))
    }

    @Test
    fun shouldNotAllowInvalidPassword() {
        given(mConfig.allowedUsername).willReturn(USERNAME)
        given(mConfig.allowedPassword).willReturn(PASSWORD)
        assert(!mCredentialsHandler.credentialsAreValid(USERNAME, "wrongpassword"))
    }
}