package com.dylantjohnson.dashboardservice.files

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class FilesControllerTests {
    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"

        private val FILES = listOf(VideoFile("video a"), VideoFile("video b"))
    }

    @Mock private lateinit var mCredentialsHandler: CredentialsHandler
    @Mock private lateinit var mFilesRepository: FilesRepository

    private lateinit var mFilesController: FilesController

    @BeforeEach
    fun initialize() {
        mFilesController = FilesController(mCredentialsHandler, mFilesRepository)
    }

    @Test
    fun shouldSendListOfFiles() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mFilesRepository.getFiles()).willReturn(FILES)
        val response = mFilesController.listFiles(USERNAME, PASSWORD)
        val files: List<VideoFile> = jacksonObjectMapper().readValue(response.body!!)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(files).isEqualTo(FILES)
    }

    @Test
    fun shouldSendNotFoundIfUnableToGetFiles() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mFilesRepository.getFiles()).willReturn(null)
        val response = mFilesController.listFiles(USERNAME, PASSWORD)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo("Not found")
    }

    @Test
    fun shouldSendUnauthorizedIfInvalidCredentialsToListFiles() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(false)
        val response = mFilesController.listFiles(USERNAME, PASSWORD)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(response.body).isEqualTo("Unauthorized")
    }
}