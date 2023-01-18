package com.grp2.a4al2androidgrp2.api.Auth

import com.grp2.a4al2androidgrp2.api.Auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiAuthControler {

    @POST("/auth/subscribe")
    fun subscribe(@Body body: SubscribeRequest) : Call<Account>
}
