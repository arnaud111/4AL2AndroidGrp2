package com.grp2.a4al2androidgrp2.api.auth

import com.grp2.a4al2androidgrp2.dto.LoginToken
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiAuthController {

    @POST("/auth/subscribe")
    fun subscribe(@Body body: SubscribeRequest) : Call<Account>

    @POST("/auth/login")
    fun login(@Body body: LoginRequest) : Call<LoginToken>

    @GET("/auth/me")
    fun me() : Call<Account>
}