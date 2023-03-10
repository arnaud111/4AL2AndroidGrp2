package com.grp2.a4al2androidgrp2.api.steam

import com.grp2.a4al2androidgrp2.dto.game.GameOpinionsResponse
import com.grp2.a4al2androidgrp2.dto.game.GameMostPlayedResponse
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.dto.game.SteamAccount
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiSteamController {

    @GET("/steamApi/getGameDetails/{steam_appId}/{lang}")
    fun getGameDetails(@Path("steam_appId") steam_appId: Int, @Path("lang") lang: String) : Call<Map<String, GameResponse>>

    @GET("/steamApi/getGameOpinions/{steam_appId}/{lang}")
    fun getGameOpinions(@Path("steam_appId") steam_appId: Int, @Path("lang") lang: String) : Call<GameOpinionsResponse>

    @GET("/steamApi/getPlayerPseudo/{steamid}")
    fun getPlayerPseudo(@Path("steamid") steamid: String) : Call<SteamAccount>

    @GET("/steamApi/getMostPlayedGames/{lang}")
    fun getGameMostPlayed(@Path("lang") lang: String) : Call<GameMostPlayedResponse>

    @GET("/steamApi/searchGames/{searched}/{lang}")
    fun searchGames(@Path("searched") searched: String, @Path("lang") lang: String) : Call<Map<String, GameResponse>>

}
