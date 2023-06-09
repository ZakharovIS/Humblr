package com.example.humblr.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: UserData
)

@JsonClass(generateAdapter = true)
data class UserData(

    @Json(name = "is_friend") val is_friend: Boolean,
    @Json(name = "snoovatar_img") val snoovatar_img: String?,
    @Json(name = "name") val name: String,
    @Json(name = "created") val created: Double?,
    @Json(name = "total_karma") val total_karma: Int?
)

@JsonClass(generateAdapter = true)
data class MyData(

    @Json(name = "snoovatar_img") val snoovatar_img: String?,
    @Json(name = "name") val name: String,
    @Json(name = "created") val created: Double?,
    @Json(name = "total_karma") val total_karma: Int?,
    @Json(name = "num_friends") val num_friends: Int?
)

@JsonClass(generateAdapter = true)
data class Friends(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: FriendChildren
)

@JsonClass(generateAdapter = true)
data class FriendChildren(
    @Json(name = "children") val friends: List<FriendData>
)

@JsonClass(generateAdapter = true)
data class FriendData(
    @Json(name = "name") val name: String,
    @Json(name = "id") val id: String,
)