package com.dylantjohnson.dashboardservice

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.dylantjohnson.dashboardservice.nyaashows.ShowRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun credentialsHandler(): CredentialsHandler {
        return CredentialsHandler()
    }

    @Bean
    fun showRepository(): ShowRepository {
        return ShowRepository()
    }
}
