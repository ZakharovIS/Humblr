package com.example.humblr.data

import android.util.Log
import com.example.humblr.api.PostsApi
import com.example.humblr.data.models.Children
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsApiRepository @Inject constructor(
    private val postsApi: PostsApi
) {

    suspend fun getNewPostsList(page: String): List<Children.ChildrenPost> {
        return postsApi.getPostListingNew(page = page)
            .body()!!.data.children as List<Children.ChildrenPost>
    }

    suspend fun getBestPostsList(page: String): List<Children.ChildrenPost> {
        return postsApi.getPostListingBest(page = page)
            .body()!!.data.children as List<Children.ChildrenPost>
    }

    suspend fun getSearchPostsList(page: String, query: String): List<Children.ChildrenPost> {
        var tmp = postsApi.getPostListingSearch(page = page, query = query)
            .body()!!.data.children as List<Children.ChildrenPost>
        Log.d("Post", "$tmp")

        return tmp
    }

    suspend fun getSinglePosts(url_post: String): Pair<Children.ChildrenPost, List<Children.ChildrenComments>> {

        val post = postsApi.getSinglePostAndComments(url = url_post).body()!!
        val listComments = mutableListOf<Children.ChildrenComments>()
        post.last().data.children.forEach {
            if (it.kind == "t1") listComments.add(it as Children.ChildrenComments)
        }

        return Pair(
            (post.first().data.children as List<Children.ChildrenPost>).first(),
            (listComments)
        )

    }

    suspend fun getPostsOfSubscribedSubredditsList(page: String): List<Children.ChildrenPost> {
        return postsApi.getPostsFromMySubreddits(page = page)
            .body()!!.data.children as List<Children.ChildrenPost>
    }

    suspend fun getSavedPostsList(page: String, name: String): List<Children.ChildrenPost> {

        val post = postsApi.getMySavedPosts(page = page, username = name).body()!!
        Log.d("Post2", "$post")
        return post.data.children as List<Children.ChildrenPost>

    }

    suspend fun savePost(postFullName: String) {
        postsApi.savePost(fullName = postFullName)

    }

    suspend fun unSavePost(postFullName: String) {
        postsApi.unsavePost(fullName = postFullName)

    }

    suspend fun voteUp(postFullName: String) {
        postsApi.vote(dir = 1, fullName = postFullName)
    }

    suspend fun voteDown(postFullName: String) {
        postsApi.vote(dir = -1, fullName = postFullName)
    }
}