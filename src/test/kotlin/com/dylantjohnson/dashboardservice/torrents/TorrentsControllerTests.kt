package com.dylantjohnson.dashboardservice.torrents

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class TorrentsControllerTests {
    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val MAGNET = "magnet"
    }

    @Mock private lateinit var mCredentialsHandler: CredentialsHandler
    @Mock private lateinit var mTorrentsRepository: TorrentsRepository

    private lateinit var mTorrentsController: TorrentsController

    @BeforeEach
    fun initialize() {
        mTorrentsController = TorrentsController(mCredentialsHandler, mTorrentsRepository)
    }

    @Test
    fun shouldAddTorrent() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        val response = mTorrentsController.addTorrent(USERNAME, PASSWORD, MAGNET)
        verify(mTorrentsRepository).addTorrent(MAGNET)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun shouldSendBadRequestIfNoMagnet() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        val response = mTorrentsController.addTorrent(USERNAME, PASSWORD, null)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body).isEqualTo("Magnet required")
    }

    @Test
    fun shouldSendNotFoundIfUnableToAddTorrent() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mTorrentsRepository.addTorrent(MAGNET)).willThrow(TorrentsRepository.AddTorrentException::class.java)
        val response = mTorrentsController.addTorrent(USERNAME, PASSWORD, MAGNET)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo("Unable to add torrent")
    }

    @Test
    fun shouldSendUnauthorizedIfBadCredentials() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(false)
        val response = mTorrentsController.addTorrent(USERNAME, PASSWORD, MAGNET)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(response.body).isEqualTo("Unauthorized")
    }
}
