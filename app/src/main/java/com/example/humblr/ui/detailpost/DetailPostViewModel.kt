package com.example.humblr.ui.detailpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.humblr.data.PostsApiRepository
import com.example.humblr.data.models.Children
import com.example.humblr.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val repository: PostsApiRepository
) : ViewModel() {

    private val _postDetail =
        MutableStateFlow<Pair<Children.ChildrenPost, List<Children.ChildrenComments>>?>(null)
    val postDetail = _postDetail.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun loadPost(url: String) {
        _state.value = State.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                _postDetail.value = repository.getSinglePosts(url)
            }.fold(
                onSuccess = {
                    _state.value = State.Success
                },
                onFailure = {
                    _state.value = State.Error
                }
            )
        }
    }

    fun savePost(id: String) {
        viewModelScope.launch {
            repository.savePost(id)
        }
    }

    fun unsavePost(id: String) {
        viewModelScope.launch {
            repository.unSavePost(id)
        }
    }

}