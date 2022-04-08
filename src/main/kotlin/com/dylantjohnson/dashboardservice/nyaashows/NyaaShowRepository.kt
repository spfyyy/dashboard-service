package com.dylantjohnson.dashboardservice.nyaashows

import org.springframework.stereotype.Component
import kotlin.text.RegexOption.DOT_MATCHES_ALL

@Component
class NyaaShowRepository(var nyaaHtmlRepository: NyaaHtmlRepository) {
    fun getShows(page: Int = 1): List<NyaaShow>? {
        val html = nyaaHtmlRepository.getHtml(page) ?: return null
        return tableRowsFrom(html).mapNotNull { showFrom(it) }
    }

    private fun tableRowsFrom(html: String): List<String> {
        val regex = """<tr.*?fa-magnet.*?</tr>""".toRegex(DOT_MATCHES_ALL)
        return regex.findAll(html).map { it.value }.toList()
    }

    private fun showFrom(tableRow: String): NyaaShow? {
        val regex = """<td colspan="2".*?<a href.*?>(.*?)</a>.*?fa-download.*?href="(.*?)"""".toRegex(DOT_MATCHES_ALL)
        val match = regex.find(tableRow) ?: return null
        val (title, link) = match.destructured
        return NyaaShow(title, link)
    }
}