package com.example.humblr.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.humblr.auth.LocalStorage
import com.example.humblr.data.PostsApiRepository
import com.example.humblr.data.SubredditsApiRepository
import com.example.humblr.data.UsersApiRepository
import com.example.humblr.data.models.Children
import com.example.humblr.domain.AllSubredditsPagingSource
import com.example.humblr.domain.SavedPostsPagingSource
import com.example.humblr.domain.SubscribedSubredditsNewPostsPagingSource
import com.example.humblr.domain.SubscribedSubredditsPagingSource
import com.example.humblr.utils.State
import com.example.humblr.utils.SwitchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val subredditsApiRepository: SubredditsApiRepository,
    private val usersApiRepository: UsersApiRepository,
    private val postsApiRepository: PostsApiRepository
) : ViewModel() {

    lateinit var subscribedSubredditsPostsNew: Flow<PagingData<Children.ChildrenPost>>
    lateinit var savedPosts: Flow<PagingData<Children.ChildrenPost>>
    lateinit var allSubreddits: Flow<PagingData<Children.ChildrenSubreddits>>
    lateinit var subscribedSubreddits: Flow<PagingData<Children.ChildrenSubreddits>>

    private val _switchStateData = MutableStateFlow(SwitchState.SUBREDDITS_ALL)
    val switchStateData = _switchStateData.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()


    init {
            loadNewPosts()
            loadAllSubreddits()
            loadSavedPosts()
            loadSubscribedSubreddits()

    }

    private fun loadNewPosts() {
        subscribedSubredditsPostsNew = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { SubscribedSubredditsNewPostsPagingSource(postsApiRepository) }
        ).flow.cachedIn(viewModelScope)
    }

    //Проверка на null!!!!
    fun loadSavedPosts() {

        savedPosts = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { SavedPostsPagingSource(postsApiRepository,LocalStorage.myName!!) }
        ).flow.cachedIn(viewModelScope)
    }

    private fun loadAllSubreddits() {
        allSubreddits = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { AllSubredditsPagingSource(subredditsApiRepository) }
        ).flow.cachedIn(viewModelScope)
    }

    private fun loadSubscribedSubreddits() {
        subscribedSubreddits = Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = { SubscribedSubredditsPagingSource(subredditsApiRepository) }
        ).flow.cachedIn(viewModelScope)
    }


    fun updateSwitchState(switchRedditsPostsIsOn: Boolean, switchAllSavedIsOn: Boolean) {

        if (switchRedditsPostsIsOn && switchAllSavedIsOn) {
            _switchStateData.value = SwitchState.POSTS_SAVED
        } else if (switchRedditsPostsIsOn && !switchAllSavedIsOn) {
            _switchStateData.value = SwitchState.POSTS_ALL
        } else if (!switchRedditsPostsIsOn && switchAllSavedIsOn) {
            _switchStateData.value = SwitchState.SUBREDDITS_SAVED
        } else {
            _switchStateData.value = SwitchState.SUBREDDITS_ALL
        }
    }

    fun updateLoadingState(
        adapterState: LoadState
    ) {
        when(adapterState) {
            LoadState.Loading -> _state.value = State.Loading
            is LoadState.NotLoading -> _state.value = State.Success
            is LoadState.Error -> _state.value = State.Error
        }
    }

    fun subscribe(name: String) {
        viewModelScope.launch {
            subredditsApiRepository.subscribeToSubreddit(name)
        }
    }

    fun unSubscribe(name: String) {
        viewModelScope.launch {
            subredditsApiRepository.unsubscribeFromSubreddit(name)
        }
    }
}


