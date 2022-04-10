package com.dylantjohnson.dashboardservice.torrents

import com.dylantjohnson.dashboardservice.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TorrentsRepositoryTests {
    companion object {
        private const val MAGNET = "link"
        private val ACTIVE_TORRENT_A = Torrent(1, "torrent a", 30)
        private val ACTIVE_TORRENT_B = Torrent(2, "torrent b", 30)

        private const val ACTIVE_OUTPUT =
                """
    ID   Done       Have  ETA           Up    Down  Ratio  Status       Name
     1    30%   148.4 MB  Done         0.0     0.0    0.0  Downloading  torrent a
     2    30%       0 MB  Done         0.0     0.0    0.0  Downloading  torrent b
Sum:            148.4 MB               0.0     0.0
                """

        private const val SOME_ACTIVE_OUTPUT =
                """
    ID   Done       Have  ETA           Up    Down  Ratio  Status       Name
     1    30%   148.4 MB  Done         0.0     0.0    0.0  Downloading  torrent a
     3   100%   148.4 MB  Done         0.0     0.0    0.0  Finished     torrent c
     2    30%       0 MB  Done         0.0     0.0    0.0  Downloading  torrent b
Sum:            296.8 MB               0.0     0.0
                """
    }

    @Mock private lateinit var mUtils: Utils

    private lateinit var mTorrentsRepository: TorrentsRepository

    @BeforeEach
    fun initialize() {
        mTorrentsRepository = TorrentsRepository(mUtils)
    }

    @Test
    fun shouldAddTorrent() {
        mTorrentsRepository.addTorrent(MAGNET)
        verify(mUtils).runProcess("transmission-remote", "--add", "\"$MAGNET\"")
    }

    @Test
    fun shouldThrowExceptionIfFailsToAddTorrent() {
        given(mUtils.runProcess("transmission-remote", "--add", "\"$MAGNET\""))
                .willThrow(Utils.ProcessErrorException::class.java)
        assertThrows<TorrentsRepository.AddTorrentException> { mTorrentsRepository.addTorrent(MAGNET) }
    }

    @Test
    fun shouldReturnListOfActiveTorrents() {
        given(mUtils.outputFromProcess("transmission-remote", "--list")).willReturn(ACTIVE_OUTPUT)
        assertThat(mTorrentsRepository.getTorrents()).isEqualTo(listOf(ACTIVE_TORRENT_A, ACTIVE_TORRENT_B))
    }

    @Test
    fun shouldReturnNullIfFailsToQueryTorrents() {
        given(mUtils.outputFromProcess("transmission-remote", "--list")).willReturn(null)
        assertThat(mTorrentsRepository.getTorrents()).isNull()
    }

    @Test
    fun shouldRemoveFinishedTorrentsFromList() {
        given(mUtils.outputFromProcess("transmission-remote", "--list")).willReturn(SOME_ACTIVE_OUTPUT)
        assertThat(mTorrentsRepository.getTorrents()).isEqualTo(listOf(ACTIVE_TORRENT_A, ACTIVE_TORRENT_B))
        verify(mUtils).runProcess("transmission-remote", "-t", "3", "--remove")
    }

    @Test
    fun shouldIgnoreTorrentRemovingError() {
        given(mUtils.outputFromProcess("transmission-remote", "--list")).willReturn(SOME_ACTIVE_OUTPUT)
        given(mUtils.runProcess("transmission-remote", "-t", "3", "--remove"))
                .willThrow(Utils.ProcessErrorException::class.java)
        assertThat(mTorrentsRepository.getTorrents()).isEqualTo(listOf(ACTIVE_TORRENT_A, ACTIVE_TORRENT_B))
    }
}