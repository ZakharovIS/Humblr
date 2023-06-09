package com.example.humblr.domain

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.humblr.data.PostsApiRepository
import com.example.humblr.data.models.Children
import javax.inject.Inject

class SavedPostsPagingSource @Inject constructor(
    private val repository: PostsApiRepository,
    private val username: String
) : PagingSource<String, Children.ChildrenPost>() {

    override fun getRefreshKey(state: PagingState<String, Children.ChildrenPost>): String = FIRST_PAGE

    @SuppressLint("SuspiciousIndentation")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Children.ChildrenPost> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getSavedPostsList(page,username)
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