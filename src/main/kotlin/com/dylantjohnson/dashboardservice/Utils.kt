package com.dylantjohnson.dashboardservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Component
class Utils {
    fun responseFrom(url: String): ResponseEntity<String> {
        val connection = URL(url).openConnection() as HttpsURLConnection
        val status = HttpStatus.valueOf(connection.responseCode)
        val body = stringFrom(connection)
        return ResponseEntity(body, status)
    }

    private fun stringFrom(connection: HttpsURLConnection): String {
        return BufferedReader(InputStreamReader(connection.inputStream)).readText()
    }
}