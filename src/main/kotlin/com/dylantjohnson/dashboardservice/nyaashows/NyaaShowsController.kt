package com.dylantjohnson.dashboardservice.nyaashows

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class NyaaShowsController(var credentialsHandler: CredentialsHandler, var nyaaShowRepository: NyaaShowRepository) {

    @PostMapping("/nyaaShows")
    fun nyaaShows(@RequestParam username: String? = null, @RequestParam password: String? = null,
            @RequestParam page: Int = 1): ResponseEntity<String> {
        if (!credentialsHandler.credentialsAreValid(username, password))
            return unauthorizedResponse()
        val shows = nyaaShowRepository.getShows(page) ?: return errorResponse()
        return successResponse(jsonContaining(shows))
    }

    private fun unauthorizedResponse() = ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)

    private fun successResponse(body: String) = ResponseEntity(body, HttpStatus.OK)

    private fun errorResponse() = ResponseEntity("Unable to fetch shows", HttpStatus.NOT_FOUND)

    private fun jsonContaining(shows: List<NyaaShow>) = jacksonObjectMapper().writeValueAsString(shows)
}
