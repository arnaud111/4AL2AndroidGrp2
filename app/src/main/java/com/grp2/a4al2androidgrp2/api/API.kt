package com.grp2.a4al2androidgrp2.api

import com.grp2.a4al2androidgrp2.api.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface API {

    @POST("/auth/subscribe")
    fun subscribe(@Body body: SubscribeRequest) : Call<Account>
}
