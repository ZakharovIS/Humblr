package com.example.humblr.ui.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.humblr.data.SubredditsApiRepository
import com.example.humblr.data.models.SubredditData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditDialogViewModel @Inject constructor(
    private val repository: SubredditsApiRepository
) : ViewModel() {

    private val _subredditInfo = MutableStateFlow<SubredditData?>(null)
    val subredditInfo = _subredditInfo.asStateFlow()

    fun loadSubredditInfo(name: String) {
        viewModelScope.launch {
            _subredditInfo.value = repository.getSubreddit(name)
        }
    }

    fun subscribe(name: String) {
        viewModelScope.launch {
            repository.subscribeToSubreddit(name)
        }
    }

    fun unSubscribe(name: String) {
        viewModelScope.launch {
            repository.unsubscribeFromSubreddit(name)
        }
    }

}