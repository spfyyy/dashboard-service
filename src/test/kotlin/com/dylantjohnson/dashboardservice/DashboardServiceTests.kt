package com.dylantjohnson.dashboardservice

import com.dylantjohnson.dashboardservice.nyaashows.NyaaShow
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShowsController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class DashboardServiceTests {
    @Autowired private lateinit var mNyaaShowsController: NyaaShowsController

    @Test
    fun shouldSendListOfShows() {
        val entity = mNyaaShowsController.nyaaShows()
        val shows: List<NyaaShow> = jacksonObjectMapper().readValue(entity.body!!)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(shows).isNotEmpty
    }
}