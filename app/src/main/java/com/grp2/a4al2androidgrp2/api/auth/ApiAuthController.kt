package com.grp2.a4al2androidgrp2.api.auth

import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiAuthController {

    @POST("/auth/subscribe")
    fun subscribe(@Body body: SubscribeRequest) : Call<Account>

    @POST("/auth/login")
    fun login(@Body body: LoginRequest) : Call<LoginToken>

    @POST("/auth/resetpassword")
    fun resetPassword(@Body body: LoginRequest) : Call<Account>

    @GET("/auth/me")
    fun me() : Call<Account>

    @GET("/auth/addLike/{steam_appId}")
    fun addLike(@Path("steam_appId") steam_appId: Int) : Call<Account>

    @GET("/auth/addWish/{steam_appId}")
    fun addWish(@Path("steam_appId") steam_appId: Int) : Call<Account>

    @GET("/auth/removeLike/{steam_appId}")
    fun removeLike(@Path("steam_appId") steam_appId: Int) : Call<Account>

    @GET("/auth/removeWish/{steam_appId}")
    fun removeWish(@Path("steam_appId") steam_appId: Int) : Call<Account>
}
