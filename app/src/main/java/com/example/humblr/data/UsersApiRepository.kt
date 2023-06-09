package com.example.humblr.data

import com.example.humblr.api.UsersApi
import com.example.humblr.data.models.MyData
import com.example.humblr.data.models.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersApiRepository @Inject constructor(
    private val usersApi: UsersApi
) {

    suspend fun getUserInfo(name: String): UserData {
        return usersApi.getUserInfo(source = name).body()!!.data
    }

    suspend fun getMyInfo(): MyData {
        return usersApi.getMyProfile().body()!!
    }

    suspend fun getFriendsList(): List<UserData> {
        val friendsList = mutableListOf<UserData>()

        val friendsShort = usersApi.getMyFriends().body()!!.data.friends

        friendsShort.forEach {
            friendsList.add(getUserInfo(it.name))
        }

        return friendsList
    }

    suspend fun friend(name: String) {
        usersApi.addToFriends(username = name)
    }

    suspend fun unfriend(name: String) {
        usersApi.deleteFromFriends(username = name)
    }


}