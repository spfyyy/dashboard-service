package com.dylantjohnson.dashboardservice

import com.dylantjohnson.dashboardservice.credentials.CredentialsHandler
import com.dylantjohnson.dashboardservice.nyaashows.NyaaHtmlRepository
import com.dylantjohnson.dashboardservice.nyaashows.NyaaShowRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun credentialsHandler(): CredentialsHandler {
        return CredentialsHandler()
    }

    @Bean
    fun nyaaShowRepository(): NyaaShowRepository {
        return NyaaShowRepository(NyaaHtmlRepository())
    }
}
