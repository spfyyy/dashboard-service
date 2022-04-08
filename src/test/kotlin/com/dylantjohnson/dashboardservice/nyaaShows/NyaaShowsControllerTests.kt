package com.dylantjohnson.dashboardservice.nyaaShows

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShowsController
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShow
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShowRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class NyaaShowsControllerTests {
    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private val SHOWS = listOf(NyaaShow("random show", "random link"),
                NyaaShow("other show", "other link"))
    }

    @Mock private lateinit var mCredentialsHandler: CredentialsHandler
    @Mock private lateinit var mNyaaShowRepository: NyaaShowRepository

    private lateinit var mNyaaShowsController: NyaaShowsController

    @BeforeEach
    fun initialize() {
        mNyaaShowsController = NyaaShowsController(mCredentialsHandler, mNyaaShowRepository)
    }

    @Test
    fun shouldSendJsonListOfShows() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mNyaaShowRepository.getShows()).willReturn(SHOWS)
        val response = mNyaaShowsController.nyaaShows(USERNAME, PASSWORD)
        val fetchedShows: List<NyaaShow> = jacksonObjectMapper().readValue(response.body!!)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(fetchedShows).isEqualTo(SHOWS)
    }

    @Test
    fun shouldSendJsonListOfShowsForNextPage() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mNyaaShowRepository.getShows(2)).willReturn(SHOWS)
        val response = mNyaaShowsController.nyaaShows(USERNAME, PASSWORD, 2)
        val fetchedShows: List<NyaaShow> = jacksonObjectMapper().readValue(response.body!!)
        verify(mNyaaShowRepository).getShows(2)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(fetchedShows).isEqualTo(SHOWS)
    }

    @Test
    fun shouldSendNotFoundWhenErrorGettingShows() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(true)
        given(mNyaaShowRepository.getShows()).willReturn(null)
        val response = mNyaaShowsController.nyaaShows(USERNAME, PASSWORD)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo("Unable to fetch shows")
    }

    @Test
    fun shouldSendUnauthorizedWhenCredentialsAreInvalid() {
        given(mCredentialsHandler.credentialsAreValid(USERNAME, PASSWORD)).willReturn(false)
        val response = mNyaaShowsController.nyaaShows(USERNAME, PASSWORD)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(response.body).isEqualTo("Unauthorized")
    }
}
