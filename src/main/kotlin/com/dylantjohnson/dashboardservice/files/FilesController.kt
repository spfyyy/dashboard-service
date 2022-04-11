package com.dylantjohnson.dashboardservice.files

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class FilesController(var credentialsHandler: CredentialsHandler, var filesRepository: FilesRepository) {
    @PostMapping("/files")
    fun listFiles(@RequestParam username: String?, @RequestParam password: String?): ResponseEntity<String> {
        if (!credentialsHandler.credentialsAreValid(username, password)) return unauthorizedResponse()
        val videoFiles = filesRepository.getFiles()
        return if (videoFiles != null) successfulListResponse(jsonContaining(videoFiles)) else notFoundResponse()
    }

    private fun successfulListResponse(body: String) = ResponseEntity<String>(body, HttpStatus.OK)

    private fun unauthorizedResponse() = ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED)

    private fun notFoundResponse() = ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND)

    private fun jsonContaining(videoFiles: List<VideoFile>) = jacksonObjectMapper().writeValueAsString(videoFiles)
}