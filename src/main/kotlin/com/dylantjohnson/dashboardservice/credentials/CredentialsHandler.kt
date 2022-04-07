package com.dylantjohnson.dashboardservice.credentials

open class CredentialsHandler {
    open fun credentialsAreValid(username: String?, password: String?): Boolean {
        throw UnsupportedOperationException()
    }
}