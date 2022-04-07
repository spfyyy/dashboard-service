package com.dylantjohnson.dashboardservice.nyaashows

open class ShowRepository {
    open fun getShows(page: Int = 1): List<Show>? {
        throw UnsupportedOperationException()
    }
}