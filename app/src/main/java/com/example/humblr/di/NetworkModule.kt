package com.example.humblr.di

import com.example.humblr.api.PostsApi
import com.example.humblr.api.SubredditsApi
import com.example.humblr.api.UsersApi
import com.example.humblr.data.models.Children
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule() {


    @Singleton
    @Provides
    fun provideMoshi() : Moshi =
        Moshi.Builder()
            .add(PolymorphicJsonAdapterFactory.of(Children::class.java, "kind")
                .withSubtype(Children.ChildrenPost::class.java, "t3")
                .withSubtype(Children.ChildrenComments::class.java, "t1")
                .withSubtype(Children.ChildrenCommentsMore::class.java,"more")
                .withSubtype(Children.ChildrenSubreddits::class.java,"t5"))
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://oauth.reddit.com/")
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .build()

    @Singleton
    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi =
        retrofit.create(PostsApi::class.java)

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

    @Singleton
    @Provides
    fun provideSubredditsApi(retrofit: Retrofit): SubredditsApi =
        retrofit.create(SubredditsApi::class.java)



}







