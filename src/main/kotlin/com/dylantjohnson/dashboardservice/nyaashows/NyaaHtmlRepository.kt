package com.dylantjohnson.dashboardservice.nyaashows

import com.dylantjohnson.dashboardservice.Config
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Component
class NyaaHtmlRepository(var config: Config) {
    fun getHtml(page: Int = 1): String? {
        return try {
            val connection = URL(config.nyaaBaseUrl.with(page)).openConnection() as HttpsURLConnection
            if (connection.responseCode == HttpStatus.OK.value()) {
                stringFrom(connection)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun stringFrom(connection: HttpsURLConnection): String {
        return BufferedReader(InputStreamReader(connection.inputStream)).readText()
    }

    fun String.with(page: Int): String {
        return if (contains("?")) {
            "$this&p=$page"
        } else {
            "$this?&p=$page"
        }
    }
}