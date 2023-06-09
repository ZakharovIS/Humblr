package com.example.humblr.data

import com.example.humblr.api.SubredditsApi
import com.example.humblr.data.models.Children
import com.example.humblr.data.models.SubredditData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubredditsApiRepository @Inject constructor(
    private val subredditsApi: SubredditsApi
) {

    suspend fun getSubreddit(name: String): SubredditData {
        return subredditsApi.getSubredditInfo(source = name).body()!!.data
    }

    suspend fun getPopularSubredditsList(page: String): List<Children.ChildrenSubreddits> {
        return subredditsApi.getAllPopularSubreddits(page = page)
            .body()!!.data.children as List<Children.ChildrenSubreddits>
    }

    suspend fun getSubscribedSubredditsList(page: String): List<Children.ChildrenSubreddits> {
        return subredditsApi.getSubscribedSubreddits(page = page)
            .body()!!.data.children as List<Children.ChildrenSubreddits>
    }

    suspend fun subscribeToSubreddit(name: String) {
        subredditsApi.subscribeToSubreddit(action = "sub", name = name)
    }

    suspend fun unsubscribeFromSubreddit(name: String) {
        subredditsApi.subscribeToSubreddit(action = "unsub", name = name)
    }
}