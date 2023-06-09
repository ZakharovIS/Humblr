package com.example.humblr.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsItems(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: CommentsData
)

@JsonClass(generateAdapter = true)
data class CommentsData(
    @Json(name = "children") val children: List<Children>
)

/*@JsonClass(generateAdapter = true)
data class CommentsChildren(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: Comment
)*/

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "body") val body: String,
    @Json(name = "score") val score: Int,
    @Json(name = "created_utc") val created: Double,
    @Json(name = "author") val author: String
)
