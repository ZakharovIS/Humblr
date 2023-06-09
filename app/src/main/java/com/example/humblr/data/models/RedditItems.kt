package com.example.humblr.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditItems(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: PostData
)

@JsonClass(generateAdapter = true)
data class PostData(
    @Json(name = "after") val after: String?,
    @Json(name = "children") val children: List<Children>,
    @Json(name = "before") val before: Any?
)

/*@JsonClass(generateAdapter = true)
data class Children(
    @Json(name = "kind") val kind: String,
    @Json(name = "data") val data: Post
)*/

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "subreddit") val subreddit: String,
    @Json(name = "selftext") val selftext: String?,
    @Json(name = "author_fullname") val author_fullname: String,
    @Json(name = "saved") val saved: Boolean,
    @Json(name = "title") val title: String,
    @Json(name = "subreddit_name_prefixed") val subreddit_name_prefixed: String,
    @Json(name = "name") val name: String,
    @Json(name = "score") val score: Int,
    @Json(name = "thumbnail") val thumbnail: String?,
    @Json(name = "post_hint") val post_hint: String?,
    @Json(name = "created") val created: Double,
    @Json(name = "url_overridden_by_dest") val url_overridden_by_dest: String?,
    @Json(name = "preview") val preview: Preview?,
    @Json(name = "subreddit_id") val subreddit_id: String,
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "num_comments") val num_comments: Int,
    @Json(name = "permalink") val permalink: String,
    @Json(name = "url") val url: String,
    @Json(name = "is_video") val is_video: Boolean,
    @Json(name = "likes") val likes: Boolean?
)

@JsonClass(generateAdapter = true)
data class Preview(
    @Json(name = "images") val images: List<ImageSource>
)

@JsonClass(generateAdapter = true)
data class ImageSource(
    @Json(name = "source") val source: Source
)

@JsonClass(generateAdapter = true)
data class Source(
    @Json(name = "url") val url: String?
)

