package com.example.humblr.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.humblr.data.UsersApiRepository
import com.example.humblr.data.models.MyData
import com.example.humblr.data.models.UserData
import com.example.humblr.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val usersApiRepository: UsersApiRepository
) : ViewModel() {

    private val _myData = MutableStateFlow<MyData?>(null)
    val myData = _myData.asStateFlow()

    private val _myFriends = MutableStateFlow<List<UserData>?>(null)
    val myFriends = _myFriends.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        getMyData()
    }


    private fun getMyData() {
        _state.value = State.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                _myData.value = usersApiRepository.getMyInfo()
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

    fun getMyFriends() {
        _state.value = State.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                _myFriends.value = usersApiRepository.getFriendsList()
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
}