package com.example.humblr.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.humblr.data.PostsApiRepository
import com.example.humblr.data.models.Children
import com.example.humblr.domain.BestPostsPagingSource
import com.example.humblr.domain.NewPostsPagingSource
import com.example.humblr.domain.SearchPostsPagingSource
import com.example.humblr.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsApiRepository: PostsApiRepository
) : ViewModel() {

    lateinit var pagedPostsNew: Flow<PagingData<Children.ChildrenPost>>
    lateinit var pagedPostsBest: Flow<PagingData<Children.ChildrenPost>>
    lateinit var pagedPostsSearch: Flow<PagingData<Children.ChildrenPost>>

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        loadNewPosts()
        loadBestPosts()
    }

    fun loadNewPosts() {
        pagedPostsNew = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { NewPostsPagingSource(postsApiRepository) }
        ).flow.cachedIn(viewModelScope)
    }

    fun loadBestPosts() {
        pagedPostsBest = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { BestPostsPagingSource(postsApiRepository) }
        ).flow.cachedIn(viewModelScope)
    }

    fun loadSearchPosts(query: String) {
        pagedPostsSearch = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { SearchPostsPagingSource(postsApiRepository, query) }
        ).flow.cachedIn(viewModelScope)
    }

    fun updateLoadingState(
        adapterState: LoadState
    ) {
        when(adapterState) {
            is LoadState.Loading -> _state.value = State.Loading
            is LoadState.NotLoading -> _state.value = State.Success
            is LoadState.Error -> _state.value = State.Error
        }
    }
}