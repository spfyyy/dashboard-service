package com.dylantjohnson.dashboardservice.torrents

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TorrentsController(var credentialsHandler: CredentialsHandler, var torrentsRepository: TorrentsRepository) {
    @PostMapping("/addTorrent")
    fun addTorrent(@RequestParam username: String?, @RequestParam password: String?,
            @RequestParam magnetUri: String?): ResponseEntity<String> {
        if (!credentialsHandler.credentialsAreValid(username, password)) return unauthorizedResponse()
        if (magnetUri == null) return badRequestResponse()
        return try {
            torrentsRepository.addTorrent(magnetUri)
            successfulResponse()
        } catch (e: TorrentsRepository.AddTorrentException) {
            unsuccessfulResponse()
        }
    }

    private fun successfulResponse() = ResponseEntity<String>(HttpStatus.CREATED)

    private fun unsuccessfulResponse() = ResponseEntity<String>("Unable to add torrent", HttpStatus.NOT_FOUND)

    private fun badRequestResponse() = ResponseEntity<String>("Magnet required", HttpStatus.BAD_REQUEST)

    private fun unauthorizedResponse() = ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED)
}