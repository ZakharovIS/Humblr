package com.example.humblr.utils

sealed class State
{
    object Loading: State()
    object Success: State()
    object Error: State()
}