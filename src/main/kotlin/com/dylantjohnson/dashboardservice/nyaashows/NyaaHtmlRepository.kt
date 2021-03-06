package com.dylantjohnson.dashboardservice.nyaashows

import com.dylantjohnson.dashboardservice.Config
import com.dylantjohnson.dashboardservice.Utils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class NyaaHtmlRepository(var config: Config, var utils: Utils) {
    fun getHtml(page: Int = 1): String? {
        val response = utils.responseFrom(config.nyaaBaseUrl.with(page)) ?: return null
        return if (response.statusCode == HttpStatus.OK) response.body else null
    }

    companion object {
        fun String.with(page: Int): String {
            return if (contains("?")) "$this&p=$page" else "$this?p=$page"
        }
    }
}