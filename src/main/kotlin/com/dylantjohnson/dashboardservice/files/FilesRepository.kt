package com.dylantjohnson.dashboardservice.files

import org.springframework.stereotype.Component

@Component
class FilesRepository {
    fun getFiles(): List<VideoFile>? {
        throw UnsupportedOperationException()
    }
}