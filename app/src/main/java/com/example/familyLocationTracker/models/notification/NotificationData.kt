package com.example.familyLocationTracker.models.notification


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationData(
    @Json(name = "body")
    var body: String?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "userId")
    var userId: String?
)