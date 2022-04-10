package com.dylantjohnson.dashboardservice.torrents

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

    @PostMapping("/torrents")
    fun listTorrents(@RequestParam username: String?, @RequestParam password: String?): ResponseEntity<String> {
        if (!credentialsHandler.credentialsAreValid(username, password)) return unauthorizedResponse()
        val torrents = torrentsRepository.getTorrents()
        return if (torrents != null) successfulListResponse(jsonContaining(torrents)) else notFoundResponse()
    }

    private fun successfulResponse() = ResponseEntity<String>(HttpStatus.CREATED)

    private fun successfulListResponse(body: String) = ResponseEntity<String>(body, HttpStatus.OK)

    private fun unsuccessfulResponse() = ResponseEntity<String>("Unable to add torrent", HttpStatus.NOT_FOUND)

    private fun badRequestResponse() = ResponseEntity<String>("Magnet required", HttpStatus.BAD_REQUEST)

    private fun unauthorizedResponse() = ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED)

    private fun notFoundResponse() = ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND)

    private fun jsonContaining(torrents: List<Torrent>) = jacksonObjectMapper().writeValueAsString(torrents)
}