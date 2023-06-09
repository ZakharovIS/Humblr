package com.example.humblr.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subreddit(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: SubredditData
)

@JsonClass(generateAdapter = true)
data class SubredditItems(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: SubredditListingData
)

@JsonClass(generateAdapter = true)
data class SubredditListingData(
    @Json(name = "after") val after: String?,
    @Json(name = "children") val children: List<Children>,
    @Json(name = "before") val before: Any?
)

@JsonClass(generateAdapter = true)
data class SubredditData(

    @Json(name = "title") val title: String?,
    @Json(name = "icon_img") val icon_img: String?,
    @Json(name = "display_name_prefixed") val display_name_prefixed: String,
    @Json(name = "subscribers") val subscribers: Int?,
    @Json(name = "name") val name: String,
    @Json(name = "created") val created: Double?,
    @Json(name = "user_is_subscriber") val user_is_subscriber: Boolean?,
    @Json(name = "public_description") val public_description: String?,
    @Json(name = "subreddit_type") val subreddit_type: String?
)