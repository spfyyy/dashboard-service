package com.dylantjohnson.dashboardservice.nyaaShows

import com.dylantjohnson.dashboardservice.Config
import com.dylantjohnson.dashboardservice.Utils
import com.dylantjohnson.dashboardservice.nyaashows.NyaaHtmlRepository
import com.dylantjohnson.dashboardservice.nyaashows.NyaaHtmlRepository.Companion.with
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@ExtendWith(MockitoExtension::class)
class NyaaHtmlRepositoryTests {
    companion object {
        private const val BASE_URL = "https://www.example.com"
        private const val HTML = "lol html"
    }

    @Mock private lateinit var mConfig: Config
    @Mock private lateinit var mUtils: Utils

    private lateinit var mNyaaHtmlRepository: NyaaHtmlRepository

    @BeforeEach
    fun initialize() {
        mNyaaHtmlRepository = NyaaHtmlRepository(mConfig, mUtils)
    }

    @Test
    fun shouldReturnHtml() {
        given(mConfig.nyaaBaseUrl).willReturn(BASE_URL)
        given(mUtils.responseFrom("$BASE_URL?p=1"))
                .willReturn(ResponseEntity(HTML, HttpStatus.OK))
        val html = mNyaaHtmlRepository.getHtml(1)
        assertThat(html).isEqualTo(HTML)
    }

    @Test
    fun shouldReturnNullIfBadResponse() {
        given(mConfig.nyaaBaseUrl).willReturn(BASE_URL)
        given(mUtils.responseFrom("$BASE_URL?p=1"))
                .willReturn(ResponseEntity(HTML, HttpStatus.NOT_FOUND))
        val html = mNyaaHtmlRepository.getHtml(1)
        assertThat(html).isNull()
    }

    @Test
    fun shouldReturnNullIfError() {
        given(mConfig.nyaaBaseUrl).willReturn(BASE_URL)
        given(mUtils.responseFrom("$BASE_URL?p=1")).willReturn(null)
        val html = mNyaaHtmlRepository.getHtml(1)
        assertThat(html).isNull()
    }

    @Test
    fun shouldAppendPageIfNoDelimeterPresent() {
        assertThat(BASE_URL.with(1)).isEqualTo("$BASE_URL?p=1")
    }

    @Test
    fun shouldAppendPageIfDelimeterPresent() {
        val url = "$BASE_URL?key=value"
        assertThat(url.with(1)).isEqualTo("$url&p=1")
    }
}