package com.example.humblr.domain

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.humblr.data.SubredditsApiRepository
import com.example.humblr.data.models.Children
import javax.inject.Inject

class AllSubredditsPagingSource @Inject constructor(
    private val repository: SubredditsApiRepository
) : PagingSource<String, Children.ChildrenSubreddits>() {

    override fun getRefreshKey(state: PagingState<String, Children.ChildrenSubreddits>): String = FIRST_PAGE

    @SuppressLint("SuspiciousIndentation")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Children.ChildrenSubreddits> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getPopularSubredditsList(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(data = it, prevKey = null,
                    nextKey = if (it.isEmpty()) null else it.last().data.name)},
            onFailure = {
                LoadResult.Error(it)}
        )
    }

    private companion object {
        private const val FIRST_PAGE = ""
    }
}