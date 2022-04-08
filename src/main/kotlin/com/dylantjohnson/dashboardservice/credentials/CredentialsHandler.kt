package com.dylantjohnson.dashboardservice.credentials

import org.springframework.stereotype.Component

@Component
class CredentialsHandler {
    fun credentialsAreValid(username: String?, password: String?): Boolean {
        throw UnsupportedOperationException()
    }
}