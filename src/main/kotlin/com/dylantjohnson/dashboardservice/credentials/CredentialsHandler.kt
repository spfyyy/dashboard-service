package com.dylantjohnson.dashboardservice.credentials

import com.dylantjohnson.dashboardservice.Config
import org.springframework.stereotype.Component

@Component
class CredentialsHandler(var config: Config) {
    fun credentialsAreValid(username: String?, password: String?): Boolean {
        if (username == null || password == null) {
            return false
        }
        return username == config.allowedUsername && password == config.allowedPassword
    }
}