package com.example.humblr.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


sealed class Children(
    open val kind: String
) {

    @JsonClass(generateAdapter = true)
    data class ChildrenPost(
        @Json(name = "kind") override val kind: String,
        @Json(name = "data") val data: Post
    ) : Children(kind)

    @JsonClass(generateAdapter = true)
    data class ChildrenComments(
        @Json(name = "kind") override val kind: String,
        @Json(name = "data") val data: Comment
    ): Children(kind)

    @JsonClass(generateAdapter = true)
    data class ChildrenCommentsMore(
        @Json(name = "kind") override val kind: String,
        @Json(name = "data") val data: More
    ): Children(kind)

    @JsonClass(generateAdapter = true)
    data class ChildrenSubreddits(
        @Json(name = "kind") override val kind: String,
        @Json(name = "data") val data: SubredditData
    ): Children(kind)
}

@JsonClass(generateAdapter = true)
data class More(
    @Json(name = "count") val count: String
)

