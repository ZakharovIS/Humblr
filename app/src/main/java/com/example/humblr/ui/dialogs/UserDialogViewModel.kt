package com.example.humblr.ui.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.humblr.data.UsersApiRepository
import com.example.humblr.data.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDialogViewModel @Inject constructor(
    private val repository: UsersApiRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserData?>(null)
    val userInfo = _userInfo.asStateFlow()

    fun loadUserInfo(name: String) {
        viewModelScope.launch {
            _userInfo.value = repository.getUserInfo(name)
        }
    }

    fun beFriends(name: String) {
        viewModelScope.launch {
            repository.friend(name)
        }
    }

    fun deleteFriend(name: String) {
        viewModelScope.launch {
            repository.unfriend(name)
        }
    }

}