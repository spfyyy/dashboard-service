package com.dylantjohnson.dashboardservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Value("\${nyaaBaseUrl}")
    lateinit var nyaaBaseUrl: String

    @Value("\${allowedUsername}")
    lateinit var allowedUsername: String

    @Value("\${allowedPassword}")
    lateinit var allowedPassword: String
}
