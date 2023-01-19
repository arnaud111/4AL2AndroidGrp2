package com.grp2.a4al2androidgrp2.api.steam

import com.grp2.a4al2androidgrp2.dto.Game
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiSteamController {

    @GET("/steamApi/getGameDetails/{steam_appId}/{lang}")
    fun getGameDetails(@Path("steam_appId") steam_appId: Int, @Path("lang") lang: String) : Call<Map<String, Game>>
}