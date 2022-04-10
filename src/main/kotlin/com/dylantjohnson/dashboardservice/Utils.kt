package com.dylantjohnson.dashboardservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Component
class Utils {
    fun responseFrom(url: String): ResponseEntity<String>? {
        return try {
            val connection = URL(url).openConnection() as HttpsURLConnection
            val status = HttpStatus.valueOf(connection.responseCode)
            val body = stringFrom(connection.inputStream)
            ResponseEntity(body, status)
        } catch (e: Exception) {
            null
        }
    }

    fun runProcess(vararg command: String) {
        val processRanSuccessfully = try {
            val process = ProcessBuilder(*command).redirectInput(ProcessBuilder.Redirect.DISCARD)
                    .redirectInput(ProcessBuilder.Redirect.DISCARD).redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
        if (!processRanSuccessfully) throw ProcessErrorException()
    }

    fun outputFromProcess(vararg command: String): String? {
        return try {
            val process = ProcessBuilder(*command).redirectInput(ProcessBuilder.Redirect.DISCARD)
                    .redirectError(ProcessBuilder.Redirect.DISCARD).start()
            val output = stringFrom(process.inputStream)
            val processRanSuccessfully = process.waitFor() == 0
            if (processRanSuccessfully) output else null
        } catch (e: Exception) {
            null
        }
    }

    private fun stringFrom(stream: InputStream): String {
        return BufferedReader(InputStreamReader(stream)).readText()
    }

    inner class ProcessErrorException : RuntimeException()
}