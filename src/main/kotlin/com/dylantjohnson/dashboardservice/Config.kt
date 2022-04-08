package com.dylantjohnson.dashboardservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Value("\${nyaaBaseUrl}")
    lateinit var nyaaBaseUrl: String
}
