package com.dylantjohnson.dashboardservice.torrents

import com.dylantjohnson.dashboardservice.Utils
import org.springframework.stereotype.Component

@Component
class TorrentsRepository(var utils: Utils) {
    fun addTorrent(magnetUri: String) {
        try {
            utils.runProcess("transmission-remote", "--add", "\"$magnetUri\"")
        } catch (e: Utils.ProcessErrorException) {
            throw AddTorrentException()
        }
    }

    fun getTorrents(): List<Torrent>? {
        return torrentsFrom(utils.outputFromProcess("transmission-remote", "--list"))
    }

    private fun torrentsFrom(transmissionOutput: String?): List<Torrent>? {
        transmissionOutput ?: return null
        val torrents = transmissionOutput.lines().mapNotNull { torrentFrom(it) }.toList()
        val completedTorrents = torrents.filter { it.percentageComplete == 100 }
        completedTorrents.forEach {
            try {
                utils.runProcess("transmission-remote", "-t", "${it.id}", "--remove")
            } catch (_: Utils.ProcessErrorException) {
            }
        }
        return torrents.filter { it.percentageComplete != 100 }
    }

    private fun torrentFrom(torrentLine: String): Torrent? {
        val regex = """\s*(\d+)\s+(\d+)%\s+.+? MB\s+.+?\s+.+?\s+.+?\s+.+?\s+.+?\s+(.*)""".toRegex()
        val matches = regex.find(torrentLine) ?: return null
        val (id, percentageComplete, name) = matches.destructured
        return Torrent(id.toInt(), name, percentageComplete.toInt())
    }

    inner class AddTorrentException : RuntimeException()
}