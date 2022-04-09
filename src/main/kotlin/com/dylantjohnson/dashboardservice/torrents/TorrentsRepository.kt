package com.dylantjohnson.dashboardservice.torrents

import org.springframework.stereotype.Component

@Component
class TorrentsRepository {
    fun addTorrent(magnetUri: String) {
        throw UnsupportedOperationException()
    }

    inner class AddTorrentException : RuntimeException()
}